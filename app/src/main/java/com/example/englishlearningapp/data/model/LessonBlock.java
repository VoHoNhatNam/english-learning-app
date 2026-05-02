package com.example.englishlearningapp.data.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class LessonBlock implements Serializable {
    private String type;
    private String content;
    private String wrong;
    private String correct;

    public LessonBlock() {
        // Required for Firestore
    }

    public LessonBlock(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public LessonBlock(String type, String content, String wrong, String correct) {
        this.type = type;
        this.content = content;
        this.wrong = wrong;
        this.correct = correct;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getWrong() { return wrong; }
    public void setWrong(String wrong) { this.wrong = wrong; }

    public String getCorrect() { return correct; }
    public void setCorrect(String correct) { this.correct = correct; }
}
