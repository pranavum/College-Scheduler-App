package com.example.firstproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ClassListAdapter extends RecyclerView.Adapter<ClassListAdapter.ViewHolder> {

    public interface OnClassClickListener {
        void onClassClick(ClassListData classData, int position);
    }

    private Context context;
    private ArrayList<ClassListData> dataArrayList;
    private OnClassClickListener listener;

    public ClassListAdapter(Context context, ArrayList<ClassListData> dataArrayList, OnClassClickListener listener) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        this.listener = listener;
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
            if (listener != null) {
                listener.onClassClick(classListData, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public interface ClassListTouchHelperContract {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(ViewHolder myViewHolder);
        void onRowClear(ViewHolder myViewHolder);
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
}
