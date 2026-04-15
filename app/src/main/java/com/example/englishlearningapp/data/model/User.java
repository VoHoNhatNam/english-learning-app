package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties // Giúp Firebase bỏ qua các trường không khớp nếu có
public class User {

    // --- Các thông tin cơ bản ---
    private String uid;
    private String username;
    private String email;
    private boolean isVip;
    private String profilePicture;

    // --- Các thông tin từ Onboarding ---
    private int age;
    private String englishLevel;
    private boolean onboardingCompleted;
    private long createdAt;

    // 1. BẮT BUỘC: Constructor rỗng để Firebase có thể ép kiểu (toObject)
    public User() {
    }

    // 2. Constructor dùng cho Đăng ký mới
    public User(String uid, String username, String email) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.isVip = false; // Mặc định mới tạo không phải VIP
        this.onboardingCompleted = false; // Mặc định chưa làm khảo sát
        this.createdAt = System.currentTimeMillis();
    }

    // 3. ĐẦY ĐỦ Getter và Setter (Firebase cần các hàm này để đọc/ghi dữ liệu)

    // --- Getter & Setter cơ bản ---
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isVip() { return isVip; }
    public void setVip(boolean vip) { this.isVip = vip; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    // --- Getter & Setter cho Onboarding ---
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getEnglishLevel() { return englishLevel; }
    public void setEnglishLevel(String englishLevel) { this.englishLevel = englishLevel; }

    public boolean isOnboardingCompleted() { return onboardingCompleted; }
    public void setOnboardingCompleted(boolean onboardingCompleted) { this.onboardingCompleted = onboardingCompleted; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}