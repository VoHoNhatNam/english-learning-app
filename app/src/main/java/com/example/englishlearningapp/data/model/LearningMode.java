package com.example.englishlearningapp.data.model;

public class LearningMode {
    private String id;
    private String title;
    private String description;
    private int iconResId;
    private boolean isRecommended;
    private boolean isSelected;

    public LearningMode(String id, String title, String description, int iconResId, boolean isRecommended, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.isRecommended = isRecommended;
        this.isSelected = isSelected;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getIconResId() { return iconResId; }
    public boolean isRecommended() { return isRecommended; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
