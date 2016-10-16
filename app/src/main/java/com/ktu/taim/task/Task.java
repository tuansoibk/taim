package com.ktu.taim.task;

/**
 * This class represents a task, it contains basic info about a task,
 * includes task name, elapsed time and the color of task presentation
 *
 * Created by A on 16/10/2016.
 */
public class Task {

    private String name;
    private long timerCount;
    private int color;

    public String getName() {
        return name;
    }

    public long getTimerCount() {
        return timerCount;
    }

    public int getColor() {
        return color;
    }

    public void setTimerCount(long timerCount) {
        this.timerCount = timerCount;
    }

    public Task(String name, int color) {
        this.name = name;
        this.timerCount = 0;
        this.color = color;
    }
}
