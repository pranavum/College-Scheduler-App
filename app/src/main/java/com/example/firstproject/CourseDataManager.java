package com.example.firstproject;

import java.util.ArrayList;
import java.util.List;

public class CourseDataManager {
    private static CourseDataManager instance;
    private List<String> courseNames = new ArrayList<>();

    // Private constructor to prevent instantiation
    private CourseDataManager() {
        // Initialize the list with "Courses" as the first item
        courseNames.add("Courses");
    }

    // Synchronized method to get the single instance of the class
    public static synchronized CourseDataManager getInstance() {
        if (instance == null) {
            instance = new CourseDataManager();
        }
        return instance;
    }

    // Getter for the course names list
    public List<String> getCourseNames() {
        return courseNames;
    }

    // Setter for the course names list that ensures "Courses" is always at the beginning
    public void setCourseNames(List<String> names) {
        List<String> updatedNames = new ArrayList<>();
        updatedNames.add("Courses"); // Ensure "Courses" is always the first item
        for (String name : names) {
            if (!"Courses".equals(name)) { // Prevent duplicate "Courses" entries
                updatedNames.add(name);
            }
        }
        this.courseNames = updatedNames;
    }
}

