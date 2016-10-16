package com.ktu.taim.task;

import java.util.Date;

/**
 * This class represents a task, it contains basic info about a task,
 * includes task name, elapsed time and the color of task presentation
 *
 * Created by A on 16/10/2016.
 */
public class Task {

    private String name;
    private long timeCounter;
    private int color;
    private long lastTimestamp;

    public String getName() {
        return name;
    }

    public long getTimeCounter() { return timeCounter; }

    public int getColor() {
        return color;
    }

    public Task(String name, int color) {
        this.name = name;
        this.timeCounter = 0;
        this.color = color;
    }

    /**
     * Prepare internal counter before start counting time.
     */
    public void prepareTimeCounter() {
        lastTimestamp = System.currentTimeMillis();
    }

    /**
     * Increase timer value.
     */
    public void updateTimeCounter() {
        long currentTimestamp = System.currentTimeMillis();
        timeCounter += currentTimestamp - lastTimestamp;
        lastTimestamp = currentTimestamp;
    }
}
