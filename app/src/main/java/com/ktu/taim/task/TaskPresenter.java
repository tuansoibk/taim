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
    private boolean isStoppedBeforeInitialized;

    public TaskFragment getFragment() {
        return taskFragment;
    }

    public Task getTask() {
        return task;
    }

    public TaskPresenter(Task task) {
        this.task = task;
        taskFragment = new TaskFragment();
        taskFragment.assignPresenter(this);
        isStoppedBeforeInitialized = false;
    }

    public void startTimeTracking() {
        if (!isStoppedBeforeInitialized) {
            initTimerTask();
            // re-base task's time counter to current time stamp
            task.prepareTimeCounter();
            timer.schedule(timerTask, 0, TIMER_UPDATE_INTERVAL);
        }
        else {
            // this presenter is already been stopped before finishing its initialization
            // reset the flag value
            System.out.println("******* OH * REALLY ****************************");
            isStoppedBeforeInitialized = false;
        }
    }

    public void stopTimeTracking() {
        if ((timer != null) && (timerTask != null)) {
            System.out.println("stop timer task");
            timerTask.cancel();
            timerTask = null;
            timer.cancel();
            timer.purge();
            timer = null;
        }
        else {
            // this function is invoked even before the initialization
            // this usually due to too quick user interactions
            isStoppedBeforeInitialized = true;
        }
    }

    private void initTimerTask() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                TaskPresenter.this.task.updateTimeCounter();
                TaskPresenter.this.getFragment().getHandler().obtainMessage().sendToTarget();
            };
        };
    }
}
