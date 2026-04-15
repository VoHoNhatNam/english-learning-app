package com.example.englishlearningapp.data.model;

import java.io.Serializable;

public class Example implements Serializable {
    private String english;
    private String vietnamese;

    public Example() {}

    public Example(String english, String vietnamese) {
        this.english = english;
        this.vietnamese = vietnamese;
    }

    public String getEnglish() { return english; }
    public void setEnglish(String english) { this.english = english; }
    public String getVietnamese() { return vietnamese; }
    public void setVietnamese(String vietnamese) { this.vietnamese = vietnamese; }
}
