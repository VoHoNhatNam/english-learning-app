package com.example.englishlearningapp.data.model;

import java.io.Serializable;
import java.util.List;

public class VocabularyLesson implements Serializable {
    private String id;
    private String title;
    private String description;
    private String level; // Beginner, Intermediate, Advanced
    private int status; // 0: locked/none, 1: completed, 2: in progress
    private List<Vocabulary> words;

    public VocabularyLesson() {}

    public VocabularyLesson(String id, String title, String description, String level, int status, List<Vocabulary> words) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.level = level;
        this.status = status;
        this.words = words;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public List<Vocabulary> getWords() { return words; }
    public void setWords(List<Vocabulary> words) { this.words = words; }
}
