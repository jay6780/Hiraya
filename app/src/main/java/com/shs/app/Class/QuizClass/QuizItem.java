package com.shs.app.Class.QuizClass;

public class QuizItem {
    private String name;

    public QuizItem() {
        // Required default constructor for Firebase deserialization
    }

    public QuizItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
