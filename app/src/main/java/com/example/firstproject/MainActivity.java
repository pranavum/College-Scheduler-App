package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.firstproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId(); // Get the ID of the selected item

            if (id == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (id == R.id.todo) {
                replaceFragment(new TodoFragment());
                return true;
            }

            return false; // Return false for any unhandled ID
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ClassListAdapter.REQUEST_CODE_EDIT_CLASS && resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra("position", -1);
            if (position != -1) {
                String className = data.getStringExtra("className");
                String classTime = data.getStringExtra("classTime");  // Handle classTime in AddClassActivity to send it back properly
                String daysOfWeek = data.getStringExtra("daysOfWeek");
                String instructorName = data.getStringExtra("instructorName");
                String location = data.getStringExtra("location");

                ClassListData updatedClass = new ClassListData(className, classTime, daysOfWeek, instructorName, location);

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).updateClassData(position, updatedClass);
                }
            }
        }
    }
}
