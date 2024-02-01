package com.example.firstproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ClassListData> dataArrayList = new ArrayList<>();
    ClassListAdapter classListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.classview); // Ensure this ID matches your RecyclerView in fragment_home.xml
        classListAdapter = new ClassListAdapter(getContext(), dataArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(classListAdapter);

        // Initial data loading
        loadInitialData();

        return view;
    }

    private void loadInitialData() {
        // Example data
        dataArrayList.add(new ClassListData("Math", "10:00 AM", "MWF", "Dr. Smith", "Room 101"));
        dataArrayList.add(new ClassListData("Physics", "11:00 AM", "TTh", "Dr. Doe", "Room 202"));
        // Add more items as needed
        classListAdapter.notifyDataSetChanged();
    }

    // Method to update data from AddClassActivity
    public void updateClassData(int position, ClassListData newData) {
        if (position >= 0 && position < dataArrayList.size()) {
            dataArrayList.set(position, newData);
            classListAdapter.notifyItemChanged(position);
        }
    }
}
