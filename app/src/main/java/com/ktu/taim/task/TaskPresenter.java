package com.ktu.taim.task;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ktu.taim.MainPresenter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class control the business logic of {@Link TaskFragment}.
 * At the moment, there is no logic here.
 *
 * Created by A on 16/10/2016.
 */
public class TaskPresenter {

    public static int TIMER_UPDATE_INTERVAL = 10;

    private TaskFragment taskFragment;
    private Task task;
    private Timer timer;
    private TimerTask timerTask;

    public TaskFragment getFragment() {
        return taskFragment;
    }
    public Task getTask() { return task; }

    public TaskPresenter(Task task) {
        this.task = task;
        taskFragment = new TaskFragment();
        taskFragment.assignPresenter(this);
        timer = new Timer();
        initTimerTask();
    }

    public void startTimeTracking() {
        // check if time counter has already been started & cancelled
        // by asserting if time counter value is greater than zero
        if (task.getTimeCounter() > 0)
        {
            // the timer had already been started & cancelled
            // just simply resume it
            resumeTimeTracking();
        }
        else {
            // this is the first time time tracking is enabled
            task.prepareTimeCounter();
            timer.schedule(timerTask, 0, TIMER_UPDATE_INTERVAL);
        }
    }

    public void stopTimeTracking() {
        timerTask.cancel();
    }

    private void resumeTimeTracking() {
        // if a timer task have been cancelled
        // we have to instance an new TimerTask object to schedule task
        initTimerTask();
        // re-base task's time counter to current time stamp
        task.prepareTimeCounter();
        timer.schedule(timerTask, 0, TIMER_UPDATE_INTERVAL);
    }

    private void initTimerTask() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TaskPresenter.this.task.updateTimeCounter();
                TaskPresenter.this.getFragment().getHandler().obtainMessage().sendToTarget();
            };
        };
    }
}
