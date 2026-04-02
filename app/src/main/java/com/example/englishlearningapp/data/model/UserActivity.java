package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserActivity {

    private String id;          // ID của bản ghi này (Document ID)
    private String userId;      // ID của người dùng (UID từ Firebase Auth)
    private String lessonId;    // ID của bài học (Nên để String nếu ID bài học là Document ID)
    private int attempts;       // Số lần làm bài
    private int mistakes;       // Số lỗi mắc phải
    private long lastUpdated;   // Thời gian cập nhật cuối cùng

    // 1. Constructor rỗng (BẮT BUỘC phải có để Firebase Firestore có thể ép kiểu)
    public UserActivity() {
    }

    // 2. Constructor đầy đủ để khởi tạo nhanh trong Code
    public UserActivity(String userId, String lessonId, int attempts, int mistakes) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.attempts = attempts;
        this.mistakes = mistakes;
        this.lastUpdated = System.currentTimeMillis();
    }

    // 3. Getter và Setter (Bắt buộc để Firebase có thể đọc/ghi dữ liệu)
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

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}