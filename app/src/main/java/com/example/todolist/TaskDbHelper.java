package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskDB";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DUE_DATE = "due_date";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_MARK_COMPLETED = "mark_completed";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_DESCRIPTION + " TEXT,"
                + KEY_DUE_DATE + " TEXT,"
                + KEY_PRIORITY + " TEXT,"
                + KEY_MARK_COMPLETED + " TEXT DEFAULT 'not_completed'" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_DESCRIPTION, task.getDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(task.getDueDate());
        values.put(KEY_DUE_DATE, formattedDate); // Store the due date as a string
        values.put(KEY_PRIORITY, task.getPriority());
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }


    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + KEY_DUE_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                String dueDateString = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DUE_DATE));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    Date dueDate = dateFormat.parse(dueDateString);
                    task.setDueDate(dueDate); // Convert the due date string to a java.util.Date
                } catch (ParseException e) {
                    // Handle parsing exception
                }
                task.setPriority(cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRIORITY)));
                task.setMarkCompleted(cursor.getString(cursor.getColumnIndexOrThrow(KEY_MARK_COMPLETED)));
                taskList.add(task);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return taskList;
    }


}
