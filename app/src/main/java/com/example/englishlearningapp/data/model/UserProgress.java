package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserProgress {

    private String id;          // ID của bản ghi (Document ID trong Firestore)
    private String userId;      // ID người dùng (UID từ Firebase Auth)
    private int lessonId;       // ID bài học
    private boolean completed;  // Trạng thái hoàn thành (true/false)
    private int score;          // Điểm số đạt được
    private long timestamp;     // Thời gian thực hiện (dùng để sắp xếp)

    // 1. BẮT BUỘC: Constructor rỗng để Firebase Firestore có thể ép kiểu (toObject)
    public UserProgress() {
    }

    // 2. Constructor đầy đủ để khởi tạo nhanh khi lưu kết quả bài học
    public UserProgress(String userId, int lessonId, boolean completed, int score) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.completed = completed;
        this.score = score;
        this.timestamp = System.currentTimeMillis();
    }

    // 3. Getter và Setter (Bắt buộc để Firebase đọc/ghi dữ liệu)
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

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}