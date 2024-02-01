package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class AddClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        EditText editTextClassName = findViewById(R.id.editTextClassName);
        EditText editTextInstructorName = findViewById(R.id.editTextInstructorName);
        EditText editTextLocation = findViewById(R.id.editTextLocation);
        TimePicker timePickerClassTime = findViewById(R.id.timePickerClassTime);

        Intent intent = getIntent();
        editTextClassName.setText(intent.getStringExtra("className"));
        editTextInstructorName.setText(intent.getStringExtra("instructorName"));
        editTextLocation.setText(intent.getStringExtra("location"));
        // Handle timePickerClassTime based on your data

        Button btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("className", editTextClassName.getText().toString());
            resultIntent.putExtra("instructorName", editTextInstructorName.getText().toString());
            resultIntent.putExtra("location", editTextLocation.getText().toString());
            // Handle timePickerClassTime result

            resultIntent.putExtra("position", intent.getIntExtra("position", -1));
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
