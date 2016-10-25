package com.ktu.taim.task;

import java.util.Date;

/**
 * This class represents a task, it contains basic info about a task,
 * includes task name, elapsed time and the color of task presentation
 *
 * Created by A on 16/10/2016.
 */
public class Task {

    private long id;
    private String name;
    private long timeCounter;
    private int color;
    private String fontName;
    private long lastTimestamp;

    public long getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String newName) { name = newName; }

    public long getTimeCounter() { return timeCounter; }

    public int getColor() {
        return color;
    }

    public String getFontName() { return fontName; }

    public Task(String name, int color) {
        this.name = name;
        this.timeCounter = 0;
        this.color = color;
        this.fontName = "";
    }

    public Task(String name, int color, String fontName) {
        this.name = name;
        this.timeCounter = 0;
        this.color = color;
        this.fontName = fontName;
    }

    /**
     * Use this method to assign database stored id to this instance
     *
     * @param id
     */
    public void assignId(long id) {
        this.id = id;
    }

    /**
     * Use this method to assign database stored time counter value to this instance
     *
     * @param timeCounter
     */
    public void assignTimeCounterValue(long timeCounter) {
        this.timeCounter = timeCounter;
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
