package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UpdateTaskActivity extends AppCompatActivity {
    private EditText taskTitleEdt;
    private EditText taskDescriptionEdt;
    private TextView selectedDate;
    private Spinner taskPriority;
    private ImageView crossButton;
    private Button btnDueDate, updateTask;
    private Date selected_date;
    private int year, month, day;

    private RelativeLayout relativeLayout1;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        taskTitleEdt = findViewById(R.id.taskTitleEdt);
        taskDescriptionEdt = findViewById(R.id.taskDescriptionEdt);
        selectedDate = findViewById(R.id.selectedDate);
        taskPriority = findViewById(R.id.taskPriority);
        crossButton = findViewById(R.id.crossButton);
        btnDueDate = findViewById(R.id.btnDueDate);
        relativeLayout1 = findViewById(R.id.relativeLayout1);
        updateTask = findViewById(R.id.updateTaskBtn);

        // Retrieve values from the intent
        Intent intent = getIntent();
        if (intent != null) {

            // Retrieve the dueDate as a Long
            long dueDateMillis = getIntent().getLongExtra("taskDueDate", 0);

// Convert dueDateMillis back to a Date or the desired format
            Date dueDate = new Date(dueDateMillis);
            selected_date = new Date(dueDateMillis);
            int taskId = getIntent().getIntExtra("taskId", -1);
            String title = intent.getStringExtra("taskTitle");
            String description = intent.getStringExtra("taskDescription");
            String priority = intent.getStringExtra("taskPriority");

            // Populate the views with the retrieved values
            taskTitleEdt.setText(title);
            taskDescriptionEdt.setText(description);
            System.out.println(dueDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(dueDate);
            selectedDate.setText("Due Date: "+formattedDate);

            btnDueDate.setVisibility(View.GONE);
            relativeLayout1.setVisibility(View.VISIBLE);

            // Set the selected priority in the spinner
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.task_priority,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            taskPriority.setAdapter(adapter);
            if (priority != null) {
                int position = adapter.getPosition(priority);
                taskPriority.setSelection(position);
            }
        }

        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDueDate.setVisibility(View.VISIBLE);
                relativeLayout1.setVisibility(View.GONE);
            }
        });

        btnDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = taskTitleEdt.getText().toString().trim();
                String taskDescription = taskDescriptionEdt.getText().toString().trim();

                if (TextUtils.isEmpty(taskTitle) || TextUtils.isEmpty(taskDescription)) {
                    // Show a toast message if title or description is empty
                    Toast.makeText(UpdateTaskActivity.this, "Task title and description cannot be empty.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if fields are empty
                }

                // Check if a date is selected
                if (selectedDate == null) {
                    // Show a toast message if no date is selected
                    Toast.makeText(UpdateTaskActivity.this, "Please select a due date.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if no date is selected
                }
                updateTaskInDatabase();
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
            selected_date = calendar.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            selectedDate.setText("Due Date: " + sdf.format(selected_date));

            btnDueDate.setVisibility(View.GONE);
            relativeLayout1.setVisibility(View.VISIBLE);

            System.out.println(selected_date);
        }
    };

    private void updateTaskInDatabase() {
        // Retrieve the updated information from the views
        String title = taskTitleEdt.getText().toString();
        String description = taskDescriptionEdt.getText().toString();
        String priority = taskPriority.getSelectedItem().toString();
        String markCompleted = "not_completed"; // You can change this as needed

        if (selected_date == null) {
            // Show a toast message if no date is selected
            Toast.makeText(UpdateTaskActivity.this, "Please select a due date.", Toast.LENGTH_SHORT).show();
            return; // Exit the method if no date is selected
        }


        Task updatedTask = new Task();
        updatedTask.setTitle(title);
        updatedTask.setDescription(description);
        updatedTask.setDueDate(selected_date); // 'selected_date' should be set when the date is selected
        updatedTask.setPriority(priority);
        updatedTask.setMarkCompleted(markCompleted);

        int taskId = getIntent().getIntExtra("taskId", -1);

        // Update the task in the database
        TaskDbHelper dbHelper = new TaskDbHelper(UpdateTaskActivity.this);
        dbHelper.updateTask(taskId, updatedTask);

        Toast.makeText(this, "Task Updated Successfully!", Toast.LENGTH_SHORT).show();
        // Finish the activity or navigate back to the task list
        finish(); // You can change this as needed
    }
}