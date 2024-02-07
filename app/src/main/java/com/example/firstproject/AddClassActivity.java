package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.firstproject.databinding.ActivityAddClassBinding;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class AddClassActivity extends AppCompatActivity {

    private ActivityAddClassBinding binding;
    private boolean isEditing = false; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddClassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish(); // This line will close the current activity
        });

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
        String daysOfWeek = getIntent().getStringExtra("daysOfWeek");
        if (daysOfWeek != null) {
            setDaysOfWeek(daysOfWeek);
        }
    }

    private void setDaysOfWeek(String daysOfWeek) {
        binding.checkBoxMonday.setChecked(daysOfWeek.contains("M"));
        binding.checkBoxTuesday.setChecked(daysOfWeek.contains("T"));
        binding.checkBoxWednesday.setChecked(daysOfWeek.contains("W"));
        binding.checkBoxThursday.setChecked(daysOfWeek.contains("TR"));
        binding.checkBoxFriday.setChecked(daysOfWeek.contains("F"));
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
                        int hour = calendar.get(Calendar.HOUR_OF_DAY); // Use HOUR for 12-hour format
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
            String className = binding.editTextClassName.getText().toString().trim();
            String instructorName = binding.editTextInstructorName.getText().toString().trim();
            String location = binding.editTextLocation.getText().toString().trim();

            // Build the days of week string
            StringBuilder daysBuilder = new StringBuilder();
            if (binding.checkBoxMonday.isChecked()) daysBuilder.append("M");
            if (binding.checkBoxTuesday.isChecked()) daysBuilder.append("T");
            if (binding.checkBoxWednesday.isChecked()) daysBuilder.append("W");
            if (binding.checkBoxThursday.isChecked()) daysBuilder.append("TR");
            if (binding.checkBoxFriday.isChecked()) daysBuilder.append("F");
            String daysOfWeek = daysBuilder.toString();

            // Validate all fields
            if (className.isEmpty() || instructorName.isEmpty() || location.isEmpty() || daysOfWeek.isEmpty()) {
                Toast.makeText(AddClassActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                return; // Exit the method if any field is empty
            }

            // No need to check time picker as it always has a value

            Intent resultIntent = new Intent();
            resultIntent.putExtra("className", className);
            resultIntent.putExtra("instructorName", instructorName);
            resultIntent.putExtra("location", location);
            resultIntent.putExtra("daysOfWeek", daysOfWeek);

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
