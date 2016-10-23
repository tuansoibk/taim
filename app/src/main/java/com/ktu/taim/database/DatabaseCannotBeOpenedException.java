package com.ktu.taim.database;

/**
 * Created by A on 22/10/2016.
 */
public class DatabaseCannotBeOpenedException extends Exception {
    public DatabaseCannotBeOpenedException() {
        super("Unable to open database");
    }
}
