package com.ktu.taim;

import android.content.Context;
import android.content.OperationApplicationException;

import com.ktu.taim.database.DataProvider;
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
            switchToTask(0);
        }
    }

    /**
     * Create a new task with specific task name.
     *
     * @param taskName
     */
    public void createNewTask(String taskName) {
        if (isReadyToPerformOperation()) {
            if (taskPresenterList.size() < MAX_TASK_COUNT) {
                int nextColor = colorPicker.getNextColor();
                Task newTask = new Task(taskName, nextColor);
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
            } else {
                activity.notifyTaskQueueFull();
            }
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
        catch (OperationApplicationException e) {
            e.printStackTrace();
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
        if (isReadyToPerformOperation()) {
            if (taskPresenterList.size() > 1) {
                int newTaskIndex = currentPresenterIndex + 1;
                newTaskIndex %= taskPresenterList.size();
                switchToTask(newTaskIndex);
            }
        }
    }

    /**
     * Switch to the task which is left next to the current task.
     */
    public void switchToLeftTask() {
        if (isReadyToPerformOperation()) {
            if (taskPresenterList.size() > 1) {
                int newTaskIndex = currentPresenterIndex - 1;
                if (newTaskIndex < 0) {
                    newTaskIndex = taskPresenterList.size() - 1;
                }
                switchToTask(newTaskIndex);
            }
        }
    }

    private void switchToTask(int newTaskIndex) {
        System.out.println(String.format("switch to new task %d from task %d", newTaskIndex, currentPresenterIndex));
        if (taskPresenterList.size() > 1) {
            TaskPresenter currentTaskPresenter = taskPresenterList.get(currentPresenterIndex);
            System.out.println("stop task: " + currentPresenterIndex);
            currentTaskPresenter.stopTimeTracking();
            TaskPresenter newTaskPresenter = taskPresenterList.get(newTaskIndex);
            currentPresenterIndex = newTaskIndex;
            activity.presentTask(newTaskPresenter);
        }
    }

    /**
     * Check if the view is ready before performing any operation
     * View is read when:
     *   - There is no task in the queue, or
     *   - View of current task is fully initialized
     *
     * @return true if the view is ready, otherwise false
     */
    private boolean isReadyToPerformOperation() {
        boolean ready = false;
        if (taskPresenterList.size() == 0) {
            ready = true;
        }
        else {
            TaskPresenter currentTask = taskPresenterList.get(currentPresenterIndex);
            ready = currentTask.getFragment().isFullyInitialized();
        }

        return ready;
    }
}
