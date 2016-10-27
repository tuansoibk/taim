package com.ktu.taim;

import android.content.Context;
import android.content.OperationApplicationException;

import com.ktu.taim.database.DataProvider;
import com.ktu.taim.database.DatabaseCannotBeOpenedException;
import com.ktu.taim.task.Task;
import com.ktu.taim.task.TaskPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class control the business logic of the main application.
 *
 * Created by A on 16/10/2016.
 */
public class MainPresenter {

    public static int MAX_TASK_COUNT = 6;

    private MainActivity activity;
    private ColorPicker colorPicker;
    private List<TaskPresenter> taskPresenterList;
    private int currentPresenterIndex;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
        this.colorPicker = new ColorPicker();
        this.taskPresenterList = new ArrayList<>();
        this.currentPresenterIndex = 0;
    }

    public void initialize(Context context) {
        System.out.println("****** INIT DB ***********************************************");
        DataProvider.initialize(context);
        loadTasksFromDatabase();
        if (taskPresenterList.size() > 0) {
            startAndPresentTask(0);
        }
    }

    public void deInitialize() {
        for (TaskPresenter taskPresenter : taskPresenterList) {
            try {
                DataProvider.getInstance().updateTask(taskPresenter.getTask());
            }
            catch (DatabaseCannotBeOpenedException e) {
                System.out.println("FATAL: unable to save task state!");
            }
        }
        System.out.println("****** DEINIT DB *********************************************");
        DataProvider.getInstance().close();
    }

    public void stopCurrentTask() {
        if ((taskPresenterList.size() > 0) && (currentPresenterIndex < taskPresenterList.size())) {
            stopTask(currentPresenterIndex);
        }
    }

    public void startCurrentTask() {
        if ((taskPresenterList.size() > 0) && (currentPresenterIndex < taskPresenterList.size())) {
            TaskPresenter taskPresenter = taskPresenterList.get(currentPresenterIndex);
            // current task has already been presented
            // just start the timer
            taskPresenter.startTimeTracking();
        }
    }

    /**
     * Create a new task with specific task name.
     *
     * @param taskName
     */
    public void createNewTask(String taskName) {
        if (taskPresenterList.size() < MAX_TASK_COUNT) {
            int nextColor = colorPicker.getNextColor();
            Task newTask = new Task(taskName, nextColor);
            try {
                // save task to db
                newTask = DataProvider.getInstance().insertTask(newTask);
            }
            catch (DatabaseCannotBeOpenedException e) {
                newTask = null;
                activity.notifyDatabaseError("Unable to save task!");
            }
            if (newTask != null) {
                TaskPresenter newPresenter = new TaskPresenter(newTask);
                int newTaskIndex = taskPresenterList.size();
                taskPresenterList.add(newPresenter);
                if (newTaskIndex == 0) {
                    // this is the very first element in the list
                    currentPresenterIndex = newTaskIndex;
                    activity.presentTask(newPresenter);
                } else {
                    switchToTask(newTaskIndex);
                }
            }
        } else {
            activity.notifyTaskQueueFull();
        }
    }

    /**
     * Load all tasks from database
     */
    public void loadTasksFromDatabase() {
        List<Task> taskList = new ArrayList<>();
        try {
            taskList = DataProvider.getInstance().getAllTasks();
        }
        catch (DatabaseCannotBeOpenedException e) {
            activity.notifyDatabaseError("Unable to load saved tasks!");
        }
        for (Task task : taskList) {
            TaskPresenter newPresenter = new TaskPresenter(task);
            taskPresenterList.add(newPresenter);
        }
    }

    /**
     * Switch to the task which is right next to the current task.
     */
    public void switchToRightTask() {
        if (taskPresenterList.size() > 1) {
            int newTaskIndex = currentPresenterIndex + 1;
            newTaskIndex %= taskPresenterList.size();
            switchToTask(newTaskIndex);
        }
    }

    /**
     * Switch to the task which is left next to the current task.
     */
    public void switchToLeftTask() {
        if (taskPresenterList.size() > 1) {
            int newTaskIndex = currentPresenterIndex - 1;
            if (newTaskIndex < 0) {
                newTaskIndex = taskPresenterList.size() - 1;
            }
            switchToTask(newTaskIndex);
        }
    }

    /**
     * Switch from the current task to new task
     *
     * @param newTaskIndex index of the new task to switch to
     */
    private void switchToTask(int newTaskIndex) {
        System.out.println(String.format("switch to new task %d from task %d", newTaskIndex, currentPresenterIndex));
        if (taskPresenterList.size() > 1) {
            stopTask(currentPresenterIndex);
            startAndPresentTask(newTaskIndex);
        }
    }

    /**
     * Start & present the task at the specific index
     *
     * @param taskIndex index of the task to be started
     */
    private void startAndPresentTask(int taskIndex) {
        System.out.println("start task: " + currentPresenterIndex);
        currentPresenterIndex = taskIndex;
        TaskPresenter taskPresenter = taskPresenterList.get(taskIndex);
        activity.presentTask(taskPresenter);
    }

    /**
     * Stop the task at the specific index
     *
     * @param taskIndex index of the task to be stopped
     */
    private void stopTask(int taskIndex) {
        System.out.println("stop task: " + currentPresenterIndex);
        TaskPresenter taskPresenter = taskPresenterList.get(taskIndex);
        taskPresenter.stopTimeTracking();
    }
    public void deleteTask(int taskIndex){
        if (taskIndex<taskPresenterList.size()) {

            taskPresenterList.get(taskIndex).selfKill();
            taskPresenterList.remove(taskIndex);
            //current task => task on the right
            if (taskIndex<taskPresenterList.size()){
                activity.presentTask(taskPresenterList.get(taskIndex));
            }
            else{
                //delete right most task => current task=> task on the left
                if (taskPresenterList.size()>0){
                    activity.presentTask(taskPresenterList.get(taskPresenterList.size() - 1));
                }
            }
        }
    }
}
