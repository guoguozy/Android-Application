package com.example.lenovo.granddictionary;

/**
 * Created by lenovo on 2020/6/4.
 */

public class WordRec {
    private int id;
    private String word;
    private String explanation;
    private int level;

    WordRec() {
    }

    WordRec(int id, String word, String explanation, int level) {
        this.id = id;
        this.word = word;
        this.explanation = explanation;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getLevel() {
        return level;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
