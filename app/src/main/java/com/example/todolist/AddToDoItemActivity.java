package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddToDoItemActivity extends AppCompatActivity {
    private Button btnDueDate, addTaskBtn;
    private int year, month, day;
    private Spinner task_priority;
    private TextView selected_date;
    private RelativeLayout relativeLayout;
    private ImageView cross_image;
    private Date selectedDate;
    private EditText taskTitleEdt, taskDescriptionEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);
        btnDueDate = findViewById(R.id.btnDueDate);
        task_priority = findViewById(R.id.taskPriority);
        selected_date = findViewById(R.id.selectedDate);
        relativeLayout = findViewById(R.id.relativeLayout1);
        cross_image = findViewById(R.id.crossButton);
        addTaskBtn = findViewById(R.id.addTaskBtn);
        taskDescriptionEdt = findViewById(R.id.taskDescriptionEdt);
        taskTitleEdt = findViewById(R.id.taskTitleEdt);

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleEdt.getText().toString().trim();
                String taskDescription = taskDescriptionEdt.getText().toString().trim();

                if (TextUtils.isEmpty(taskTitle) || TextUtils.isEmpty(taskDescription)) {
                    // Show a toast message if title or description is empty
                    Toast.makeText(AddToDoItemActivity.this, "Task title and description cannot be empty.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if fields are empty
                }

                // Check if a date is selected
                if (selectedDate == null) {
                    // Show a toast message if no date is selected
                    Toast.makeText(AddToDoItemActivity.this, "Please select a due date.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if no date is selected
                }

                // Now you can add the task to the database
                addTaskToDatabase(taskTitle, taskDescription, selectedDate, task_priority.getSelectedItem().toString());

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.task_priority,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        task_priority.setAdapter(adapter);

        // Initialize the DatePicker to the current date.
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        cross_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDueDate.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
            }
        });


    }

    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Update the TextInputLayout with the selected date.
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            selectedDate = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            selected_date.setText("Due Date: " + sdf.format(selectedDate));

            btnDueDate.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);

            System.out.println(selectedDate);
        }
    };

    private void addTaskToDatabase(String title, String description, Date dueDate, String priority) {
        TaskDbHelper dbHelper = new TaskDbHelper(this);

        // Create a Task object with the task details
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);

        // Convert java.util.Date to java.sql.Date
        java.sql.Date sqlDueDate = new java.sql.Date(dueDate.getTime());
        task.setDueDate(sqlDueDate);

        task.setPriority(priority);

        // Insert the task into the database
        dbHelper.addTask(task);

        // Optionally, you can show a success message
        Toast.makeText(AddToDoItemActivity.this, "Task added successfully.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
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
        } else if (item.getItemId() == R.id.contact) {
            Intent intent = new Intent(getApplicationContext(), SendSmsActivity.class);
            startActivity(intent);
        }
        return true;
    }
}