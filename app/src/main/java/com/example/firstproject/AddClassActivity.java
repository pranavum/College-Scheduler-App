package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.firstproject.databinding.ActivityAddClassBinding;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddClassActivity extends AppCompatActivity {

    private ActivityAddClassBinding binding;
    private boolean isEditing = false; // Flag to check if we are adding a new class or editing an existing one

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Check if we're in editing mode
        isEditing = getIntent().getBooleanExtra("editing", false);
        if (isEditing) {
            populateExistingData();
        }

        setupTimePicker();
        setupDoneButton();
    }

    private void populateExistingData() {
        binding.editTextClassName.setText(getIntent().getStringExtra("className"));
        binding.editTextInstructorName.setText(getIntent().getStringExtra("instructorName"));
        binding.editTextLocation.setText(getIntent().getStringExtra("location"));
        // Parsing and setting the class time and days of the week will be done in setupTimePicker() and setupSpinner()
        String daysOfWeek = getIntent().getStringExtra("daysOfWeek");
        if (daysOfWeek != null) {
            setDaysOfWeek(daysOfWeek);
        }
    }

    private void setDaysOfWeek(String daysOfWeek) {
        binding.radioButtonMonday.setChecked(daysOfWeek.contains("M"));
        binding.radioButtonTuesday.setChecked(daysOfWeek.contains("T"));
        binding.radioButtonWednesday.setChecked(daysOfWeek.contains("W"));
        binding.radioButtonThursday.setChecked(daysOfWeek.contains("Th"));
        binding.radioButtonFriday.setChecked(daysOfWeek.contains("F"));
    }

    private void setupTimePicker() {
        // Set TimePicker to not use 24-hour view to enable AM/PM selector
        binding.timePickerClassTime.setIs24HourView(false);

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
                        binding.timePickerClassTime.setHour(hour);
                        binding.timePickerClassTime.setMinute(minute);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Handle the case where the class time string is in an unexpected format
                }
            }
        }
    }


    private void setupDoneButton() {
        binding.btnDone.setOnClickListener(v -> {
            String className = binding.editTextClassName.getText().toString().trim(); // Trim to remove leading and trailing spaces

            // Check if the class name is empty
            if (className.equals("")) {
                // Inform the user that class name is required
                Toast.makeText(AddClassActivity.this, "Class name is required.", Toast.LENGTH_SHORT).show();
                return; // Exit the method without proceeding further
            }

            // Continue with your existing code if class name is not empty
            Intent resultIntent = new Intent();
            resultIntent.putExtra("className", className);
            resultIntent.putExtra("instructorName", binding.editTextInstructorName.getText().toString());
            resultIntent.putExtra("location", binding.editTextLocation.getText().toString());

            StringBuilder daysBuilder = new StringBuilder();
            if (binding.radioButtonMonday.isChecked()) daysBuilder.append("M");
            if (binding.radioButtonTuesday.isChecked()) daysBuilder.append("T");
            if (binding.radioButtonWednesday.isChecked()) daysBuilder.append("W");
            if (binding.radioButtonThursday.isChecked()) daysBuilder.append("Th");
            if (binding.radioButtonFriday.isChecked()) daysBuilder.append("F");

            resultIntent.putExtra("daysOfWeek", daysBuilder.toString());

            // Get the hour and minute from the TimePicker
            int hour = binding.timePickerClassTime.getHour();
            int minute = binding.timePickerClassTime.getMinute();

            // Convert 24-hour time to 12-hour time format with AM/PM
            String amPm = hour >= 12 ? "PM" : "AM";
            hour = hour > 12 ? hour - 12 : (hour == 0 ? 12 : hour);
            String classTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour, minute, amPm);

            resultIntent.putExtra("classTime", classTime);

            if (isEditing) {
                // Include the position if we're editing an existing class
                int position = getIntent().getIntExtra("position", -1);
                resultIntent.putExtra("position", position);
            }

            setResult(RESULT_OK, resultIntent);
            finish(); // Finish activity and return to the previous one
        });
    }

}
