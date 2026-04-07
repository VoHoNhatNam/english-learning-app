package com.example.englishlearningapp.data.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Model class đại diện cho kết quả bài kiểm tra (Quiz) của sinh viên.
 * Implement Serializable để có thể truyền object này qua Bundle nếu cần.
 */
public class TestResult implements Serializable {

    private String userId;
    private int lessonId;
    private int score; // Điểm số (thường là %)
    private long timeSpent; // Thời gian làm bài (giây)
    private Date createdAt; // Ngày làm bài

    // 1. Constructor mặc định (BẮT BUỘC để Firebase Firestore có thể map dữ liệu)
    public TestResult() {
    }

    // 2. Constructor đầy đủ để khởi tạo nhanh khi vừa làm xong Quiz
    public TestResult(String userId, int lessonId, int score, long timeSpent, Date createdAt) {
        this.userId = userId;
        this.lessonId = lessonId;
        this.score = score;
        this.timeSpent = timeSpent;
        this.createdAt = createdAt;
    }

    // 3. Getters và Setters (Để Firebase truy cập các trường dữ liệu)
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}