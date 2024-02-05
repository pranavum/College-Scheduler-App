package com.example.firstproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ToDoListData> toDoList;

    private OnItemClickListener listener;

    // Interface for handling click events on items and checkboxes within the list
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onCheckBoxClick(int position, boolean isChecked);
    }

    // Constructor to initialize context, to-do list data, and click listener
    public ToDoListAdapter(Context context, ArrayList<ToDoListData> toDoList, OnItemClickListener listener) {
        this.context = context;
        this.toDoList = toDoList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoListData item = toDoList.get(position);
        // Set the data for each list item based on the ToDoListData object
        holder.taskName.setText(item.getTaskName());
        holder.taskType.setText(item.getTaskType());
        holder.dueTime.setText(item.getDueTime());
        holder.dueDate.setText(item.getDueDate());
        holder.course.setText(item.getCourse());
        holder.location.setText(item.getLocation());
        holder.taskCompleted.setChecked(item.isCompleted());
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }


    // Method to update the list data and refresh the RecyclerView
    public void updateData(ArrayList<ToDoListData> newData) {
        this.toDoList = newData;
        notifyDataSetChanged(); // Notify any registered observers that the data set has changed
    }

    // ViewHolder class to hold the views for each list item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskType, dueTime, dueDate, course, location;
        CheckBox taskCompleted;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            // Initialize the views
            taskName = itemView.findViewById(R.id.listName);
            taskType = itemView.findViewById(R.id.todo_type);
            dueTime = itemView.findViewById(R.id.dueTime);
            dueDate = itemView.findViewById(R.id.dueDate);
            course = itemView.findViewById(R.id.course);
            location = itemView.findViewById(R.id.location);
            taskCompleted = itemView.findViewById(R.id.checkBoxTodo);

            // Set an onClickListener for the whole list item view
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            // Set an onClickListener for the checkbox within the list item
            taskCompleted.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCheckBoxClick(position, taskCompleted.isChecked());
                    }
                }
            });
        }
    }
}