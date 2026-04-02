package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserProfile {

    private String userId;       // ID người dùng từ Firebase Auth
    private String age;          // Để String hoặc int tùy vào cách bạn lưu (thường chọn String để khớp với text nút bấm)
    private String school;       // Trường học (nếu có)
    private String englishLevel; // Trình độ (Beginner, Inter, Advanced)
    private long updatedAt;      // Thời gian cập nhật cuối cùng

    // 1. BẮT BUỘC: Constructor rỗng để Firebase Firestore có thể ép kiểu (Deserialization)
    public UserProfile() {
    }

    // 2. Constructor đầy đủ để khởi tạo nhanh khi người dùng hoàn tất Onboarding
    public UserProfile(String userId, String age, String englishLevel) {
        this.userId = userId;
        this.age = age;
        this.englishLevel = englishLevel;
        this.updatedAt = System.currentTimeMillis();
    }

    // 3. Getter và Setter (Bắt buộc để Firebase đọc/ghi dữ liệu)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEnglishLevel() {
        return englishLevel;
    }

    public void setEnglishLevel(String englishLevel) {
        this.englishLevel = englishLevel;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}