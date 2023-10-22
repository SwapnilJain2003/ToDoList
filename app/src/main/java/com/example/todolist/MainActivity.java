package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
        taskAdapter = new TaskAdapter(this, taskList);
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

    private void fetchTasksFromDatabase() {
        // Create an instance of your TaskDbHelper to interact with the database
        TaskDbHelper dbHelper = new TaskDbHelper(this);

        // Use the getAllTasks method to retrieve tasks from the database
        taskList.clear(); // Clear the existing list
        taskList.addAll(dbHelper.getAllTasks()); // Add the tasks from the database
    }
}
