package com.example.englishlearningapp.data.model;

import java.io.Serializable;
import java.util.List;

public class ReadingArticle implements Serializable {
    private String id;
    private String title;
    private String author;
    private String authorRole;
    private String authorImageUrl;
    private String readTime;
    private String imageUrl;
    private String content; // Tiếng Anh
    private String contentVi; // Tiếng Việt
    private String level; // Beginner, Intermediate, Advanced
    private String aiInsights;
    private List<String> keywords;

    public ReadingArticle() {}

    public ReadingArticle(String id, String title, String author, String authorRole, String readTime, String content, String contentVi, String level, String aiInsights) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.authorRole = authorRole;
        this.readTime = readTime;
        this.content = content;
        this.contentVi = contentVi;
        this.level = level;
        this.aiInsights = aiInsights;
    }

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getAuthorRole() { return authorRole; }
    public void setAuthorRole(String authorRole) { this.authorRole = authorRole; }
    public String getAuthorImageUrl() { return authorImageUrl; }
    public void setAuthorImageUrl(String authorImageUrl) { this.authorImageUrl = authorImageUrl; }
    public String getReadTime() { return readTime; }
    public void setReadTime(String readTime) { this.readTime = readTime; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getContentVi() { return contentVi; }
    public void setContentVi(String contentVi) { this.contentVi = contentVi; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getAiInsights() { return aiInsights; }
    public void setAiInsights(String aiInsights) { this.aiInsights = aiInsights; }
    public List<String> getKeywords() { return keywords; }
    public void setKeywords(List<String> keywords) { this.keywords = keywords; }
}
