package com.example.englishlearningapp.data.model;

import java.io.Serializable;

public class GrammarLesson implements Serializable {
    private String id;
    private String title;
    private String description;
    private String content;
    private String level; // Beginner, Intermediate, Advanced
    private int status; // 0: locked/none, 1: completed, 2: in progress

    // Required for Firestore toObject()
    public GrammarLesson() {}

    public GrammarLesson(String id, String title, String description, int status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
}
