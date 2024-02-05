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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.firstproject.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements ClassListAdapter.OnClassClickListener {

    private FragmentHomeBinding binding;
    private ArrayList<ClassListData> dataArrayList;
    private ClassListAdapter classListAdapter;
    private ActivityResultLauncher<Intent> addClassLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        setupRecyclerView();
        setupItemTouchHelper();
        setupAddClassLauncher();
        updateSharedViewModelCourses();

        return view;
    }

    private void setupRecyclerView() {
        dataArrayList = loadClassData(); // Load saved data
        if (dataArrayList == null) {
            dataArrayList = new ArrayList<>();
        }
        classListAdapter = new ClassListAdapter(getContext(), dataArrayList, this);
        binding.classview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.classview.setAdapter(classListAdapter);
    }
    public void onRowMoved(int fromPosition, int toPosition) {
        Collections.swap(dataArrayList, fromPosition, toPosition);
        classListAdapter.notifyItemMoved(fromPosition, toPosition);
        updateSharedViewModelCourses();
        saveClassData();
    }
    private void setupItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                onRowMoved(fromPosition, toPosition);
                return true;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                dataArrayList.remove(position); // Remove the item from your data set
                classListAdapter.notifyItemRemoved(position); // Notify the adapter of the removal
                updateSharedViewModelCourses();
                saveClassData(); // Save the updated data set
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                // This method is called when the interaction with an element is over and it also completed its animation
                updateSharedViewModelCourses();
                saveClassData();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.classview); // Attach to your RecyclerView
    }



    private void setupAddClassLauncher() {
        addClassLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                Intent data = result.getData();
                ClassListData classData = new ClassListData(
                        data.getStringExtra("className"),
                        data.getStringExtra("classTime"),
                        data.getStringExtra("daysOfWeek"),
                        data.getStringExtra("instructorName"),
                        data.getStringExtra("location"));

                // Check if the result includes a position
                if (data.hasExtra("position")) {
                    // Update existing item
                    int position = data.getIntExtra("position", -1);
                    if (position != -1 && position < dataArrayList.size()) {
                        dataArrayList.set(position, classData);
                        classListAdapter.notifyItemChanged(position);
                    }
                } else {
                    // Add new item
                    dataArrayList.add(classData);
                    classListAdapter.notifyItemInserted(dataArrayList.size() - 1);
                }



                saveClassData(); // Save the updated list
            }
        });
    }

    public void addClassItem(Intent data) {
        ClassListData newClass = new ClassListData(
                data.getStringExtra("className"),
                data.getStringExtra("classTime"),
                data.getStringExtra("daysOfWeek"),
                data.getStringExtra("instructorName"),
                data.getStringExtra("location"));
        dataArrayList.add(newClass);
        updateSharedViewModelCourses();
        classListAdapter.notifyDataSetChanged();
        saveClassData(); // Save the updated list
    }

    @Override
    public void onClassClick(ClassListData classData, int position) {
        Intent intent = new Intent(getActivity(), AddClassActivity.class);
        intent.putExtra("editing", true);
        // Pass existing class data to AddClassActivity
        intent.putExtra("className", classData.getClassName());
        intent.putExtra("classTime", classData.getClassTime());
        intent.putExtra("daysOfWeek", classData.getDaysOfWeek());
        intent.putExtra("instructorName", classData.getInstructorName());
        intent.putExtra("location", classData.getLocation());
        intent.putExtra("position", position);

        addClassLauncher.launch(intent); // Launch AddClassActivity with launcher for editing
    }

    private void saveClassData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ClassData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(dataArrayList);
        editor.putString("classList", json);
        editor.apply();
    }

    private ArrayList<ClassListData> loadClassData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("ClassData", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("classList", null);
        Type type = new TypeToken<ArrayList<ClassListData>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void updateSharedViewModelCourses() {
        List<String> courseNames = new ArrayList<>();
        for (ClassListData classData : dataArrayList) {
            courseNames.add(classData.getClassName()); // Assuming getClassName() returns the course name
        }
        CourseDataManager.getInstance().setCourseNames(courseNames);
    }
}
