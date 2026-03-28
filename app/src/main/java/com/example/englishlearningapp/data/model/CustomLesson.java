package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties // Bỏ qua các thuộc tính lạ khi map dữ liệu từ Firebase
public class CustomLesson {

    private String id;          // ID của bài học (Document ID)
    private String userId;      // ID của người tạo bài học
    private String title;       // Tiêu đề bài học
    private String description; // Mô tả bài học (Thêm mới)
    private long createdAt;     // Thời gian tạo để sắp xếp (Thêm mới)

    // 1. Constructor rỗng (BẮT BUỘC phải có để Firebase có thể ép kiểu dữ liệu)
    public CustomLesson() {
    }

    // 2. Constructor đầy đủ tham số (Dùng khi bạn tạo mới object trong Code)
    public CustomLesson(String id, String userId, String title, String description, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    // 3. Getter và Setter (Bắt buộc để Firebase và Adapter truy cập dữ liệu)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}