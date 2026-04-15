package com.example.englishlearningapp.data.model;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_AI = 1;

    private String text;
    private int type;
    private long timestamp;
    private String label; // For "CONVERSATION STARTER"
    private boolean hasGrammarFix;
    private String correction;

    public ChatMessage(String text, int type) {
        this.text = text;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public String getText() { return text; }
    public int getType() { return type; }
    public long getTimestamp() { return timestamp; }
    
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public boolean isHasGrammarFix() { return hasGrammarFix; }
    public void setHasGrammarFix(boolean hasGrammarFix) { this.hasGrammarFix = hasGrammarFix; }

    public String getCorrection() { return correction; }
    public void setCorrection(String correction) { this.correction = correction; }
}