package com.shs.app.Class.Student;


public class Student2 {
    private String id;
    private String email;
    private String image;
    private String name;
    private String username;
    private String writtenWorks;
    private String performanceTask;
    private String quarterlyAssessment;

    public Student2() {
        // Default constructor required for Firebase
    }

    public Student2(String id, String email, String image, String name, String username,
                    String writtenWorks, String performanceTask, String quarterlyAssessment) {
        this.id = id;
        this.email = email;
        this.image = image;
        this.name = name;
        this.username = username;
        this.writtenWorks = writtenWorks;
        this.performanceTask = performanceTask;
        this.quarterlyAssessment = quarterlyAssessment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWrittenWorks() {
        return writtenWorks;
    }

    public void setWrittenWorks(String writtenWorks) {
        this.writtenWorks = writtenWorks;
    }

    public String getPerformanceTask() {
        return performanceTask;
    }

    public void setPerformanceTask(String performanceTask) {
        this.performanceTask = performanceTask;
    }

    public String getQuarterlyAssessment() {
        return quarterlyAssessment;
    }

    public void setQuarterlyAssessment(String quarterlyAssessment) {
        this.quarterlyAssessment = quarterlyAssessment;
    }
}
