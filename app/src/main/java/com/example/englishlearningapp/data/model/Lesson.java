package com.example.englishlearningapp.data.model;

import java.io.Serializable;

/**
 * Model đại diện cho một bài học (Lesson) - Dùng để nhận dữ liệu từ Firebase
 */
public class Lesson implements Serializable {
    private int id;
    private String title;
    private String description;
    private String category;

    // Constructor mặc định bắt buộc cho Firebase Firestore
    public Lesson() {}

    public Lesson(int id, String title, String description, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}