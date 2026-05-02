package com.example.englishlearningapp.data.model;

import java.io.Serializable;

public class Vocabulary implements Serializable {

    private int id;
    private int lessonId;
    private String word;
    private String meaning;
    private String example;
    private String exampleMeaning;
    private String phonetic; // Phiên âm (vd: /həˈləʊ/)
    private String type;     // Loại từ (n, v, adj...)

    public Vocabulary() {}

    public Vocabulary(String word, String type, String meaning) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
    }

    public Vocabulary(String word, String type, String meaning, String phonetic) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
        this.phonetic = phonetic;
    }

    public Vocabulary(String word, String type, String meaning, String phonetic, String example) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
        this.phonetic = phonetic;
        this.example = example;
    }

    public Vocabulary(String word, String type, String meaning, String phonetic, String example, String exampleMeaning) {
        this.word = word;
        this.type = type;
        this.meaning = meaning;
        this.phonetic = phonetic;
        this.example = example;
        this.exampleMeaning = exampleMeaning;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }
    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }
    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }
    public String getExampleMeaning() { return exampleMeaning; }
    public void setExampleMeaning(String exampleMeaning) { this.exampleMeaning = exampleMeaning; }
    public String getPhonetic() { return phonetic; }
    public void setPhonetic(String phonetic) { this.phonetic = phonetic; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
