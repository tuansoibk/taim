package com.ktu.taim.task;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ktu.taim.MainPresenter;

/**
 * This class control the business logic of {@Link TaskFragment}.
 * At the moment, there is no logic here.
 *
 * Created by A on 16/10/2016.
 */
public class TaskPresenter {

    private TaskFragment taskFragment;
    private Task task;

    public TaskFragment getFragment() {
        return taskFragment;
    }

    public TaskPresenter(Task task) {
        this.task = task;
        taskFragment = TaskFragment.newInstance(task.getName(), task.getColor());
    }
}
