package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.firstproject.databinding.ActivityAddTodoBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTodoActivity extends AppCompatActivity {

    private ActivityAddTodoBinding binding;
    private boolean isEditing = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTodoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isEditing = getIntent().getBooleanExtra("editing", false);

        setupTypesSpinner();
        setupTimePicker();
        setupDatePicker();
        setupDoneButton();
        setupCoursesSpinner();

        if (isEditing) {
            populateExistingData();
        }
    }

    private void setupTimePicker() {
        // Set TimePicker to not use 24-hour view to enable AM/PM selector
        binding.timePicker.setIs24HourView(false);

        if (isEditing) {
            // Assuming classTime is passed in "hh:mm a" format for AM/PM
            String classTime = getIntent().getStringExtra("classTime");
            if (classTime != null && !classTime.isEmpty()) {
                try {
                    // Use SimpleDateFormat to parse the classTime string
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                    Date date = sdf.parse(classTime);
                    Calendar calendar = Calendar.getInstance();
                    if (date != null) {
                        calendar.setTime(date);
                        int hour = calendar.get(Calendar.HOUR); // Use HOUR for 12-hour format
                        int minute = calendar.get(Calendar.MINUTE);
                        binding.timePicker.setHour(hour);
                        binding.timePicker.setMinute(minute);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle the case where the class time string is in an unexpected format
                }
            }
        }
    }

    private void setupTypesSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.todo_types, android.R.layout.simple_spinner_item); // Ensure you have task_types array in your resources
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerToDoTypes.setAdapter(adapter);

        if (isEditing) {
            String taskType = getIntent().getStringExtra("taskType");
            if (taskType != null) {
                int spinnerPosition = adapter.getPosition(taskType);
                binding.spinnerToDoTypes.setSelection(spinnerPosition);
            }
        }
    }

    private void setupCoursesSpinner() {
        // Fetch the course names from the CourseDataManager singleton
        List<String> courseNames = CourseDataManager.getInstance().getCourseNames();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courseNames);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        binding.spinnerCourses.setAdapter(adapter);

        if (isEditing) {
            // Check if there's a course name passed with the intent and set the spinner position to that course
            String courseName = getIntent().getStringExtra("course");
            if (courseName != null && !courseName.isEmpty()) {
                int spinnerPosition = adapter.getPosition(courseName);
                binding.spinnerCourses.setSelection(spinnerPosition);
            }
        }
    }


    private void setupDatePicker() {
        final Calendar c = Calendar.getInstance();
        binding.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTodoActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> binding.editTextDate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private void setupDoneButton() {
        binding.btnDone.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            // Add data to resultIntent
            resultIntent.putExtra("taskName", binding.editTextTodoName.getText().toString());
            resultIntent.putExtra("taskType", binding.spinnerToDoTypes.getSelectedItem().toString());
            resultIntent.putExtra("dueDate", binding.editTextDate.getText().toString());
            resultIntent.putExtra("location", binding.editTextLocation.getText().toString());
            if ("Other".equals(binding.spinnerToDoTypes.getSelectedItem().toString()) || "Courses".equals(binding.spinnerCourses.getSelectedItem().toString())) {
                // If "Other" is selected, set course to ""
                resultIntent.putExtra("course", "");
            } else {
                // Otherwise, use the selected course
                resultIntent.putExtra("course", binding.spinnerCourses.getSelectedItem().toString());
            }
            // Get the hour and minute from the TimePicker
            int hour = binding.timePicker.getHour();
            int minute = binding.timePicker.getMinute();

            // Convert 24-hour time to 12-hour time format with AM/PM
            String amPm = hour >= 12 ? "PM" : "AM";
            hour = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
            String dueTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);

            resultIntent.putExtra("dueTime", dueTime);

            if (isEditing) {
                int position = getIntent().getIntExtra("position", -1);
                resultIntent.putExtra("position", position);
            }

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void populateExistingData() {
        // Populate existing data for editing
        binding.editTextTodoName.setText(getIntent().getStringExtra("taskName"));
        binding.editTextDate.setText(getIntent().getStringExtra("dueDate"));
        binding.editTextLocation.setText(getIntent().getStringExtra("location"));
        // Task type will be set in setupSpinner() based on existing data
    }
}
