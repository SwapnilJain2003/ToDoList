package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.window.OnBackInvokedDispatcher;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddToDoItemActivity extends AppCompatActivity {
    private TextInputLayout tilDueDate;
    private Button btnDueDate;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do_item);

        tilDueDate = findViewById(R.id.tilDueDate);
        btnDueDate = findViewById(R.id.btnDueDate);

        // Initialize the DatePicker to the current date.
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePickerListener, year, month, day);
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Update the TextInputLayout with the selected date.
            tilDueDate.getEditText().setText(new StringBuilder()
                    .append(year).append("-")
                    .append(String.format("%02d", month + 1)).append("-")
                    .append(String.format("%02d", day)));
        }
    };

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}