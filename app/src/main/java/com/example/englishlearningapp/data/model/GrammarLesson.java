package com.example.englishlearningapp.data.model;

public class GrammarLesson {
    private String id;
    private String title;
    private String description;
    private int status; // 0: locked/none, 1: completed (check), 2: in progress (refresh)

    public GrammarLesson(String id, String title, String description, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStatus() { return status; }
}