package com.example.firstproject;

public class ToDoListData {
    private String taskName;
    private String taskType; // Assuming a simple String to represent the selected item from the Spinner
    private String dueDate;
    private String course;
    private String location;
    private boolean isCompleted;

    // Constructor
    public ToDoListData(String taskName, String taskType, String dueDate, String course, String location, boolean isCompleted) {
        this.taskName = taskName;
        this.taskType = taskType;
        this.dueDate = dueDate;
        this.course = course;
        this.location = location;
        this.isCompleted = isCompleted;
    }

    // Getters
    public String getTaskName() {
        return taskName;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getCourse() {
        return course;
    }

    public String getLocation() {
        return location;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}

