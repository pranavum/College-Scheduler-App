package com.example.firstproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.example.firstproject.databinding.FragmentTodoBinding;
import android.app.AlertDialog;
import android.widget.Spinner;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;


public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;
    private ArrayList<ToDoListData> toDoListDataArrayList;
    private ToDoListAdapter toDoListAdapter;
    private ActivityResultLauncher<Intent> addToDoLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupRecyclerView();
        setupAddToDoLauncher();
        setupSwipeToDelete();

        Button filterButton = view.findViewById(R.id.filterButton); // Make sure you have a filterButton in your fragment's layout
        filterButton.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    private void setupRecyclerView() {
        toDoListDataArrayList = loadToDoData();
        if (toDoListDataArrayList == null) {
            toDoListDataArrayList = new ArrayList<>();
        }
        toDoListAdapter = new ToDoListAdapter(getContext(), toDoListDataArrayList, new ToDoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click, possibly for editing
                Intent intent = new Intent(getActivity(), AddTodoActivity.class);
                intent.putExtra("editing", true);
                // Pass existing ToDo data to AddTodoActivity
                intent.putExtras(packToDoDataBundle(toDoListDataArrayList.get(position), position));
                addToDoLauncher.launch(intent);
            }

            @Override
            public void onCheckBoxClick(int position, boolean isChecked) {
                // Update the item's completion status and save
                toDoListDataArrayList.get(position).setCompleted(isChecked);
                toDoListAdapter.notifyItemChanged(position);
                saveToDoData();
            }
        });
        binding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.listview.setAdapter(toDoListAdapter);
    }



    private void setupAddToDoLauncher() {
        addToDoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                // Extract ToDo item data from intent
                ToDoListData toDoData = unpackToDoDataBundle(result.getData());
                int position = result.getData().getIntExtra("position", -1);

                if (position >= 0 && position < toDoListDataArrayList.size()) {
                    // Update existing item
                    toDoListDataArrayList.set(position, toDoData);
                    toDoListAdapter.notifyItemChanged(position);
                } else {
                    // Add new item
                    toDoListDataArrayList.add(toDoData);
                    toDoListAdapter.notifyItemInserted(toDoListDataArrayList.size() - 1);
                }

                saveToDoData(); // Save the updated list
            }
        });
    }

    public void addToDoItem(Intent data) {
        // Unpack the ToDo item data from the intent
        String taskName = data.getStringExtra("taskName");
        String taskType = data.getStringExtra("taskType");
        String dueTime = data.getStringExtra("dueTime");
        String dueDate = data.getStringExtra("dueDate");
        String course = data.getStringExtra("course");
        String location = data.getStringExtra("location");
        boolean isCompleted = data.getBooleanExtra("isCompleted", false);

        ToDoListData toDoData = new ToDoListData(taskName, taskType, dueTime, dueDate, course, location, isCompleted);

        // Check if the result includes a position for an existing item
        if (data.hasExtra("position")) {
            // Update existing item
            int position = data.getIntExtra("position", -1);
            if (position != -1 && position < toDoListDataArrayList.size()) {
                toDoListDataArrayList.set(position, toDoData);
                toDoListAdapter.notifyItemChanged(position);
            }
        } else {
            // Add new item
            toDoListDataArrayList.add(toDoData);
            toDoListAdapter.notifyItemInserted(toDoListDataArrayList.size() - 1);
        }

        saveToDoData(); // Save the updated list
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Move not supported
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                toDoListDataArrayList.remove(position); // Remove the item
                toDoListAdapter.notifyItemRemoved(position);
                saveToDoData(); // Save the changes
            }
        };

        new ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(binding.listview);
    }

    private ArrayList<ToDoListData> loadToDoData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ToDoData", Context.MODE_PRIVATE);
        String json = prefs.getString("toDoList", null);
        Type type = new TypeToken<ArrayList<ToDoListData>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private void saveToDoData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ToDoData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("toDoList", new Gson().toJson(toDoListDataArrayList));
        editor.apply();
    }

    private Bundle packToDoDataBundle(ToDoListData toDoData, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("taskName", toDoData.getTaskName());
        bundle.putString("taskType", toDoData.getTaskType());
        bundle.putString("dueTime", toDoData.getDueTime());
        bundle.putString("dueDate", toDoData.getDueDate());
        bundle.putString("course", toDoData.getCourse());
        bundle.putString("location", toDoData.getLocation());
        bundle.putBoolean("isCompleted", toDoData.isCompleted());
        bundle.putInt("position", position);
        return bundle;
    }

    private ToDoListData unpackToDoDataBundle(Intent data) {
        String taskName = data.getStringExtra("taskName");
        String taskType = data.getStringExtra("taskType");
        String dueTime = data.getStringExtra("dueTime");
        String dueDate = data.getStringExtra("dueDate");
        String course = data.getStringExtra("course");
        String location = data.getStringExtra("location");
        boolean isCompleted = data.getBooleanExtra("isCompleted", false);
        return new ToDoListData(taskName, taskType, dueTime, dueDate, course, location, isCompleted);
    }

    public void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(dialogView);

        RadioGroup sortOptionsGroup = dialogView.findViewById(R.id.sortOptionsGroup);
        RadioButton sortByDateButton = dialogView.findViewById(R.id.sortByDate);
        Spinner courseSpinner = dialogView.findViewById(R.id.courseFilterSpinner);

        // Populate the spinner with courses
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CourseDataManager.getInstance().getCourseNames());
        courseSpinner.setAdapter(adapter);

        AlertDialog dialog = builder.create();

        Button applyButton = dialogView.findViewById(R.id.applyFilterButton); // Adjust ID as necessary
        applyButton.setOnClickListener(v -> {
            String selectedTaskType = getSelectedTaskType(sortOptionsGroup);
            boolean sortByDate = sortByDateButton.isChecked();
            String selectedCourse = courseSpinner.getSelectedItem().toString();
            sortTasks(selectedTaskType, selectedCourse, sortByDate);
            dialog.dismiss();
        });

        dialog.show();
    }

    private String getSelectedTaskType(RadioGroup sortOptionsGroup) {
        int selectedId = sortOptionsGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.sortByType) {
            return "Exam";
        } else if (selectedId == R.id.sortByDueDate) {
            return "Assignment";
        } else if (selectedId == R.id.sortByCourse) {
            return "Other";
        } else {
            return null; // Or any default value
        }
    }

    private void sortTasks(String selectedTaskType, String selectedCourse, boolean sortByDate) {
        Collections.sort(toDoListDataArrayList, new Comparator<ToDoListData>() {
            @Override
            public int compare(ToDoListData o1, ToDoListData o2) {
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm a"); // "a" for AM/PM marker
                try {
                    // Sort by due date if sortByDate is true
                    if (sortByDate) {
                        Date date1 = dateTimeFormat.parse(o1.getDueDate() + " " + o1.getDueTime());
                        Date date2 = dateTimeFormat.parse(o2.getDueDate() + " " + o2.getDueTime());
                        if (date1 != null && date2 != null) {
                            int dateComparison = date1.compareTo(date2);
                            if (dateComparison != 0) {
                                return dateComparison;
                            }
                            // If dates are equal, continue to compare by task type and course
                        } else if (date1 != null) {
                            return -1; // Non-null dates come before null dates
                        } else if (date2 != null) {
                            return 1;  // Non-null dates come before null dates
                        }
                    }

                    // Sort by task type if types are specified and not equal
                    if (selectedTaskType != null && !selectedTaskType.isEmpty()) {
                        boolean isO1Type = o1.getTaskType().equals(selectedTaskType);
                        boolean isO2Type = o2.getTaskType().equals(selectedTaskType);
                        if (isO1Type && !isO2Type) return -1; // Selected task type comes first
                        if (!isO1Type && isO2Type) return 1;  // Selected task type comes first
                    }

                    // Finally, sort by course if courses are specified and not equal
                    if (selectedCourse != null && !selectedCourse.isEmpty()) {
                        boolean isO1Course = o1.getCourse().equals(selectedCourse);
                        boolean isO2Course = o2.getCourse().equals(selectedCourse);
                        if (isO1Course && !isO2Course) return -1; // Selected course comes first
                        if (!isO1Course && isO2Course) return 1;  // Selected course comes first
                    }

                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing error
                }

                return 0; // No change in order if all criteria are equal
            }
        });

        toDoListAdapter.notifyDataSetChanged(); // Notify the adapter to refresh the RecyclerView
    }





}