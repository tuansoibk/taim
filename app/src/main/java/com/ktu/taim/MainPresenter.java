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
            int newIndex = taskPresenterList.size();
            TaskPresenter newPresenter = new TaskPresenter(newTask);
            currentPresenterIndex = taskPresenterList.size();
            taskPresenterList.add(newPresenter);
            activity.presentTask(newPresenter);
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
            currentPresenterIndex++;
            currentPresenterIndex %= taskPresenterList.size();
            TaskPresenter rightTaskPresenter = taskPresenterList.get(currentPresenterIndex);
            activity.presentTask(rightTaskPresenter);
        }
    }

    /**
     * Switch to the task which is left next to the current task.
     */
    public void switchToLeftTask() {
        if (taskPresenterList.size() > 1) {
            currentPresenterIndex--;
            if (currentPresenterIndex < 0) {
                currentPresenterIndex = taskPresenterList.size() - 1;
            }
            TaskPresenter leftTaskPresenter = taskPresenterList.get(currentPresenterIndex);
            activity.presentTask(leftTaskPresenter);
        }
    }
}
