package com.example.englishlearningapp.data.model;

import java.io.Serializable;

public class Vocabulary implements Serializable {

    private int id;
    private int lessonId;
    private String word;
    private String meaning;
    private String example;
    private String phonetic; // Bổ sung phiên âm cho bậc đại học

    // 1. Constructor rỗng (Bắt buộc cho Firebase Firestore)
    public Vocabulary() {
    }

    // 2. Constructor đầy đủ để tạo dữ liệu nhanh
    public Vocabulary(int id, int lessonId, String word, String phonetic, String meaning, String example) {
        this.id = id;
        this.lessonId = lessonId;
        this.word = word;
        this.phonetic = phonetic;
        this.meaning = meaning;
        this.example = example;
    }

    // 3. Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }
}