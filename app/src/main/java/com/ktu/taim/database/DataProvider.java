package com.ktu.taim.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ktu.taim.task.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A on 20/10/2016.
 */
public class DataProvider extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "com.ktu.taim.db";
    private static final int DATABASE_VERSION = 1;

    private static DataProvider instance;

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new DataProvider(context);
        }
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            throw new DataProviderNotYetInitializedException();
        }

        return instance;
    }

    public DataProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DatabaseContract.TaskTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DatabaseContract.TaskTable.upgradeTable(db, oldVersion, newVersion);
    }

    public Task insertTask(Task task) throws OperationApplicationException {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.TaskTable.COLUMN_NAME, task.getName());
            values.put(DatabaseContract.TaskTable.COLUMN_TIME_COUNTER, task.getTimeCounter());
            values.put(DatabaseContract.TaskTable.COLUMN_COLOR, task.getColor());
            values.put(DatabaseContract.TaskTable.COLUMN_FONT, task.getFontName());
            long id = db.insert(DatabaseContract.TaskTable.TABLE_NAME, null, values);
            task.assignId(id);
        } else {
            throw new OperationApplicationException("Unable to open database.");
        }

        return task;
    }

    public boolean updateTask(Task task) throws OperationApplicationException {
        boolean updated = false;
        if (task != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db != null) {
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.TaskTable.COLUMN_NAME, task.getName());
                values.put(DatabaseContract.TaskTable.COLUMN_TIME_COUNTER, task.getTimeCounter());
                values.put(DatabaseContract.TaskTable.COLUMN_COLOR, task.getColor());
                values.put(DatabaseContract.TaskTable.COLUMN_FONT, task.getFontName());
                String selection = DatabaseContract.TaskTable.COLUMN_ID + " = ?";
                String[] selectionArgs = { String.valueOf(task.getId()) };
                int count = db.update(DatabaseContract.TaskTable.TABLE_NAME, values,
                        selection, selectionArgs);
                updated = count > 0;
            }
            else {
                throw new OperationApplicationException("Unable to open database.");
            }
        }

        return updated;
    }

    public boolean deleteTask(Task task) throws OperationApplicationException {
        boolean deleted = false;
        if (task != null) {
            SQLiteDatabase db = this.getWritableDatabase();
            if (db != null) {
                String selection = DatabaseContract.TaskTable.COLUMN_ID + " = ?";
                String[] selectionArgs = { String.valueOf(task.getId()) };
                int count = db.delete(DatabaseContract.TaskTable.TABLE_NAME,
                        selection, selectionArgs);
                deleted = count > 0;
            }
            else {
                throw new OperationApplicationException("Unable to open database.");
            }
        }

        return deleted;
    }

    public List<Task> getAllTasks() throws OperationApplicationException {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String[] projection = {
                    DatabaseContract.TaskTable.COLUMN_ID,
                    DatabaseContract.TaskTable.COLUMN_NAME,
                    DatabaseContract.TaskTable.COLUMN_TIME_COUNTER,
                    DatabaseContract.TaskTable.COLUMN_COLOR,
                    DatabaseContract.TaskTable.COLUMN_FONT
            };
            Cursor c = db.query(DatabaseContract.TaskTable.TABLE_NAME, projection, null, null,
                    null, null, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    Task task = createNewItemFromCurrentCursorPosition(c);
                    taskList.add(task);
                } while (c.moveToNext());
            }
        }
        else {
            throw new OperationApplicationException("Unable to open database.");
        }

        return taskList;
    }

    public Task createNewItemFromCurrentCursorPosition(Cursor c) {
        Task task = null;
        if (c != null) {
            long id = c.getLong(c.getColumnIndex(DatabaseContract.TaskTable.COLUMN_ID));
            String name = c.getString(c.getColumnIndex(DatabaseContract.TaskTable.COLUMN_NAME));
            long timeCounter = c.getLong(c.getColumnIndex(DatabaseContract.TaskTable.COLUMN_TIME_COUNTER));
            int color = c.getInt(c.getColumnIndex(DatabaseContract.TaskTable.COLUMN_COLOR));
            String fontName = c.getString(c.getColumnIndex(DatabaseContract.TaskTable.COLUMN_FONT));
            task = new Task(name, color, fontName);
            task.assignId(id);
        }

        return task;
    }
}
