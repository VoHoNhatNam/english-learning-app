package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class CustomLesson implements Serializable {

    private String id;
    private String userId;
    private String title;
    private String description;
    private String level; // A1, A2, B1, B2...

    // Nội dung bài học
    private String grammarPoint;    // Điểm ngữ pháp (Tên)
    private String grammarContent;  // Nội dung chi tiết ngữ pháp
    private String readingTitle;    // Tiêu đề bài đọc
    private String readingContent;  // Nội dung bài đọc

    private List<Vocabulary> vocabularies; // Danh sách từ vựng đi kèm bài học
    private List<Example> examples;       // Danh sách ví dụ song ngữ

    private long createdAt;

    // 1. Constructor rỗng (BẮT BUỘC cho Firebase)
    public CustomLesson() {
        this.createdAt = System.currentTimeMillis();
    }

    // 2. Constructor rút gọn để tạo dữ liệu mẫu nhanh (Tránh lỗi ở Fragment)
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
        // Nếu đã truyền createdAt từ ngoài vào thì dùng tham số đó,
        // nếu truyền 0 hoặc muốn lấy mới thì dùng System.currentTimeMillis()
        this.createdAt = (createdAt > 0) ? createdAt : System.currentTimeMillis();
    }

    // --- Getter và Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getGrammarPoint() { return grammarPoint; }
    public void setGrammarPoint(String grammarPoint) { this.grammarPoint = grammarPoint; }

    public String getGrammarContent() { return grammarContent; }
    public void setGrammarContent(String grammarContent) { this.grammarContent = grammarContent; }

    public String getReadingTitle() { return readingTitle; }
    public void setReadingTitle(String readingTitle) { this.readingTitle = readingTitle; }

    public String getReadingContent() { return readingContent; }
    public void setReadingContent(String readingContent) { this.readingContent = readingContent; }

    public List<Vocabulary> getVocabularies() { return vocabularies; }
    public void setVocabularies(List<Vocabulary> vocabularies) { this.vocabularies = vocabularies; }

    public List<Example> getExamples() { return examples; }
    public void setExamples(List<Example> examples) { this.examples = examples; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}