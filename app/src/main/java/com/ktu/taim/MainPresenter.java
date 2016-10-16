package com.ktu.taim;

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

    /**
     * Create a new task with specific task name.
     *
     * @param taskName
     */
    public void createNewTask(String taskName) {
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
            }
            else {
                switchToTask(newTaskIndex);
            }
        }
        else {
            activity.notifyTaskQueueFull();
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

    private void switchToTask(int newTaskIndex) {
        if (taskPresenterList.size() > 1) {
            TaskPresenter currentTaskPresenter = taskPresenterList.get(currentPresenterIndex);
            currentTaskPresenter.stopTimeTracking();
            TaskPresenter newTaskPresenter = taskPresenterList.get(newTaskIndex);
            currentPresenterIndex = newTaskIndex;
            activity.presentTask(newTaskPresenter);
        }
    }
}
