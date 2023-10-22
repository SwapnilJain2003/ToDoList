package com.example.todolist;

import java.util.Date;

public class Task {
    private int id;
    private String title;
    private String description;
    private Date dueDate;
    private String priority;
    private String markCompleted;

    public Task() {
        // Default constructor
    }

    public Task(String title, String description, Date dueDate, String priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.markCompleted = "not_completed"; // Set a default value
    }

    // Getters and Setters for the fields
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMarkCompleted() {
        return markCompleted;
    }

    public void setMarkCompleted(String markCompleted) {
        this.markCompleted = markCompleted;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", priority='" + priority + '\'' +
                ", markCompleted='" + markCompleted + '\'' +
                '}';
    }
}
