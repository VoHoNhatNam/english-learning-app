package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties // Giúp Firebase bỏ qua các trường không khớp nếu có
public class User {
    private String id;
    private String name;
    private String email;
    private String password; // Lưu ý: Chỉ dùng cho đăng ký bằng Email/Pass
    private boolean isVip;
    private String profilePicture; // Thêm trường này nếu dùng Google Login

    // 1. BẮT BUỘC: Constructor rỗng để Firebase có thể ép kiểu (toObject)
    public User() {
    }

    // 2. Constructor dùng cho Đăng ký mới (Email/Pass)
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isVip = false; // Mặc định mới tạo không phải VIP
    }

    // 3. ĐẦY ĐỦ Getter và Setter (Firebase cần các hàm này để đọc/ghi dữ liệu)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isVip() { return isVip; }
    public void setVip(boolean vip) { isVip = vip; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}