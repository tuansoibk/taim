package com.ktu.taim.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by A on 20/10/2016.
 */
public class DatabaseContract {

    // private constructor to prevent user from instance this class
    private DatabaseContract() {};

    public static class TaskTable implements BaseColumns {
        public static final String TABLE_NAME = "Tasks";
        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_TIME_COUNTER = "TimeCounter";
        public static final String COLUMN_COLOR = "Color";
        public static final String COLUMN_FONT = "Font";

        private static final String CREATE_TABLE = "create table if not exists "
                + TABLE_NAME
                + "("
                + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_NAME + " text not null, "
                + COLUMN_TIME_COUNTER + " bigint, "
                + COLUMN_COLOR + " integer, "
                + COLUMN_FONT + " text null"
                + ")";

        public static void createTable(SQLiteDatabase database) {
            database.execSQL(CREATE_TABLE);
        }

        public static void upgradeTable(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.w(TaskTable.class.getName(), "Upgrading database from version "
                    + oldVersion + " to " + newVersion
                    + ", which will destroy all existing data.");
            database.execSQL("drop table if exists " + TABLE_NAME);
        }
    }
}
