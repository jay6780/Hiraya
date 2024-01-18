package com.shs.app.Class.grade;

public class Grade {
    private String adminId;
    private String userId;
    private String fileName;
    private String grade;
    private String performanceTask;
    private String selectedUser;
    private String selectedUserId;

    public Grade() {
        // Default constructor required for Firebase
    }

    public Grade(String adminId, String userId, String fileName, String grade, String performanceTask, String selectedUser, String selectedUserId) {
        this.adminId = adminId;
        this.userId = userId;
        this.fileName = fileName;
        this.grade = grade;
        this.performanceTask = performanceTask;
        this.selectedUser = selectedUser;
        this.selectedUserId = selectedUserId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPerformanceTask() {
        return performanceTask;
    }

    public void setPerformanceTask(String performanceTask) {
        this.performanceTask = performanceTask;
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(String selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }
}
