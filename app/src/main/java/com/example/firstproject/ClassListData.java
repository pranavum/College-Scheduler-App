package com.example.firstproject;

public class ClassListData {
    private String className;
    private String classTime; // Consider using a Date or Calendar object for real applications
    private String daysOfWeek; // This could be a List<String> for more flexibility
    private String instructorName;
    private String location;

    // Constructor
    public ClassListData(String className, String classTime, String daysOfWeek, String instructorName, String location) {
        this.className = className;
        this.classTime = classTime;
        this.daysOfWeek = daysOfWeek;
        this.instructorName = instructorName;
        this.location = location;
    }

    // Getter for class name
    public String getClassName() {
        return className;
    }

    // Getter for class time
    public String getClassTime() {
        return classTime;
    }

    // Getter for days of the week
    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    // Getter for instructor name
    public String getInstructorName() {
        return instructorName;
    }

    // Getter for location
    public String getLocation() {
        return location;
    }

    // Optionally, you can add setter methods if you need to modify the fields after object creation.
}
