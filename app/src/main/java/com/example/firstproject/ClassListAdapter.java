package com.example.firstproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ClassListData> dataArrayList;

    public ClassListAdapter(Context context, ArrayList<ClassListData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClassListData classListData = dataArrayList.get(position);
        holder.className.setText(classListData.getClassName());
        holder.classTime.setText(classListData.getClassTime());
        holder.classDays.setText(classListData.getDaysOfWeek());
        holder.instructor.setText(classListData.getInstructorName());
        holder.location.setText(classListData.getLocation());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddClassActivity.class);
            intent.putExtra("className", classListData.getClassName());
            intent.putExtra("classTime", classListData.getClassTime());
            intent.putExtra("daysOfWeek", classListData.getDaysOfWeek());
            intent.putExtra("instructorName", classListData.getInstructorName());
            intent.putExtra("location", classListData.getLocation());
            intent.putExtra("position", position);
            ((AppCompatActivity) context).startActivityForResult(intent, 1);  // Ensure this request code is unique
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView className, classTime, classDays, instructor, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            className = itemView.findViewById(R.id.className);
            classTime = itemView.findViewById(R.id.classTime);
            classDays = itemView.findViewById(R.id.classDays);
            instructor = itemView.findViewById(R.id.instructor);
            location = itemView.findViewById(R.id.location);
        }
    }

    public void updateClassData(int position, ClassListData newData) {
        dataArrayList.set(position, newData);
        notifyItemChanged(position);
    }
    public static final int REQUEST_CODE_EDIT_CLASS = 1;
}
