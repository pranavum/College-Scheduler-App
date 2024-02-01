package com.example.firstproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TodoFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<ToDoListData> dataArrayList = new ArrayList<>();
    ToDoListAdapter toDoListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = view.findViewById(R.id.listview); // Ensure this ID in your fragment's layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        toDoListAdapter = new ToDoListAdapter(getContext(), dataArrayList);
        recyclerView.setAdapter(toDoListAdapter);

        // Populate your toDoList here, for example:
        dataArrayList.add(new ToDoListData("Read Chapter 5", "Study", "2024-03-15", "Biology", "Library", false));
        toDoListAdapter.notifyDataSetChanged();

        return view;
    }
}
