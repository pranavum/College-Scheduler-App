package com.example.firstproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.firstproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ActivityResultLauncher<Intent> addClassLauncher;
    private ActivityResultLauncher<Intent> addToDoLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());
        setupAddClassLauncher();
        setupAddToDoLauncher();
        setupFloatingActionButton();

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

    private void setupAddClassLauncher() {
        addClassLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).addClassItem(data);
                }
            }
        });
    }

    private void setupAddToDoLauncher() {
        addToDoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
                if (fragment instanceof TodoFragment) {
                    ((TodoFragment) fragment).addToDoItem(data); // Assuming TodoFragment has a method addToDoItem to handle new/edited ToDos
                }
            }
        });
    }

    private void setupFloatingActionButton() {
        binding.fabAddClass.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
            if (currentFragment instanceof HomeFragment) {
                // Open AddClassActivity
                Intent intent = new Intent(MainActivity.this, AddClassActivity.class);
                addClassLauncher.launch(intent);
            } else if (currentFragment instanceof TodoFragment) {
                // Open AddTodoActivity
                Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                addToDoLauncher.launch(intent);
            }
        });
    }
}
