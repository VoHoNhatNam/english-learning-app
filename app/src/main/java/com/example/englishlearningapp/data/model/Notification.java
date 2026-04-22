package com.example.englishlearningapp.data.model;

import java.io.Serializable;

public class Notification implements Serializable {
    public enum Type {
        LESSON, ACHIEVEMENT, COMMUNITY, OFFER, SYSTEM
    }

    private String id;
    private String title;
    private String content;
    private String time;
    private Type type;
    private boolean isRead;
    private boolean isDeleted;
    private String actionText;
    private String badgeText;

    public Notification(String id, String title, String content, String time, Type type, boolean isRead) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.type = type;
        this.isRead = isRead;
        this.isDeleted = false;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public Type getType() { return type; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { isDeleted = deleted; }
    public String getActionText() { return actionText; }
    public void setActionText(String actionText) { this.actionText = actionText; }
    public String getBadgeText() { return badgeText; }
    public void setBadgeText(String badgeText) { this.badgeText = badgeText; }
}
