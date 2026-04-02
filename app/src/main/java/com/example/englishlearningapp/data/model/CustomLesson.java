package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CustomLesson {

    private String id;
    private String userId;
    private String title;
    private String description;
    private long createdAt;

    // 1. Constructor rỗng (BẮT BUỘC cho Firebase)
    public CustomLesson() {
    }

    // 2. MỚI THÊM: Constructor rút gọn để tạo dữ liệu mẫu nhanh (Hết lỗi đỏ ở Fragment)
    public CustomLesson(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
    }

    // 3. Constructor đầy đủ (Dùng khi lấy dữ liệu từ Firestore hoặc tạo đầy đủ)
    public CustomLesson(String id, String userId, String title, String description, long createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        // Sửa lại: Nếu đã truyền createdAt từ ngoài vào thì dùng tham số đó,
        // nếu truyền 0 hoặc muốn lấy mới thì dùng System.currentTimeMillis()
        this.createdAt = (createdAt > 0) ? createdAt : System.currentTimeMillis();
    }

    // --- Getter và Setter (Giữ nguyên) ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}