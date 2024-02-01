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

    public ToDoListAdapter(Context context, ArrayList<ToDoListData> toDoList) {
        this.context = context;
        this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToDoListData item = toDoList.get(position);
        holder.taskName.setText(item.getTaskName());
        holder.taskType.setText(item.getTaskType());
        holder.dueDate.setText(item.getDueDate());
        holder.course.setText(item.getCourse());
        holder.location.setText(item.getLocation());
        holder.taskCompleted.setChecked(item.isCompleted());
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView taskName, taskType, dueDate, course, location;
        CheckBox taskCompleted; // Assuming you want to track completion status

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.listName);
            taskType = itemView.findViewById(R.id.todo_type);
            dueDate = itemView.findViewById(R.id.dueDate);
            course = itemView.findViewById(R.id.course);
            location = itemView.findViewById(R.id.location);
            taskCompleted = itemView.findViewById(R.id.checkBoxTodo);
        }
    }
}

