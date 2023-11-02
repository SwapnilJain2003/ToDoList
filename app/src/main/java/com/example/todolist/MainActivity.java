package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskCheckedChangeListener {
    FloatingActionButton addToDoItem;
    RecyclerView recyclerView;
    TaskAdapter taskAdapter;
    List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList, this); // Pass the listener
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        addToDoItem = findViewById(R.id.addToDoItem);
        addToDoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDoItemActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Fetch tasks from the database and update the taskList
        fetchTasksFromDatabase();
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskCheckedChanged(Task task) {
        // Update the database with the new 'mark_completed' value
        updateTaskInDatabase(task);
    }

    private void updateTaskInDatabase(Task task) {
        TaskDbHelper dbHelper = new TaskDbHelper(this);

        // Update the 'mark_completed' field in the database
        dbHelper.updateTaskMarkCompleted(task.getId(), task.getMarkCompleted());
    }

    private void fetchTasksFromDatabase() {
        // Create an instance of your TaskDbHelper to interact with the database
        TaskDbHelper dbHelper = new TaskDbHelper(this);

        // Use the getAllTasks method to retrieve tasks from the database
        taskList.clear(); // Clear the existing list
        taskList.addAll(dbHelper.getAllTasks()); // Add the tasks from the database
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.logout){
            TaskDbHelper dbHelper = new TaskDbHelper(this);
            dbHelper.clearDatabase();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId()==R.id.contact) {
            Intent intent = new Intent(getApplicationContext(), SendSmsActivity.class);
            startActivity(intent);
        }
        return true;
    }

    void openUpdateTaskActivity(Task task) {
        Intent intent = new Intent(getApplicationContext(), UpdateTaskActivity.class);

        // Convert the dueDate to milliseconds (assuming dueDate is a Date or Calendar object)
        long dueDateMillis = task.getDueDate().getTime();

// Pass the dueDateMillis to the intent
        intent.putExtra("taskDueDate", dueDateMillis);

        // Pass the task details to the update task activity
        intent.putExtra("taskId", task.getId());
        intent.putExtra("taskTitle", task.getTitle());
        intent.putExtra("taskDescription", task.getDescription());
        intent.putExtra("taskDueDate", task.getDueDate().getTime()); // Convert to milliseconds
        intent.putExtra("taskPriority", task.getPriority());
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    void deleteTask(Task task) {
        TaskDbHelper dbHelper = new TaskDbHelper(this);
        dbHelper.deleteTask(task.getId());
        // Remove the task from the list and notify the adapter
        taskList.remove(task);
        taskAdapter.notifyDataSetChanged();
    }


}

