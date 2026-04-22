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
    
    private int status; // 0: Locked/Chưa học, 1: Completed/Đã thuộc, 2: In progress/Đang học
    private long createdAt;

    public CustomLesson() {
        this.createdAt = System.currentTimeMillis();
        this.status = 0;
    }

    public CustomLesson(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = System.currentTimeMillis();
        this.status = 0;
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
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
