package com.shs.app.Class.comment;

public class Comment {
    private String name;
    private String messageText;
    private String image;
    private String uid;
    private String profession;
    private String email;
    private String imageContent;
    private String filename;
    private String fileUrl; // New property for file URL

    private String id;
    private String userId;
    private String announcementId;
    // Empty constructor (required for Firebase)
    public Comment() {
    }

    public Comment(String name, String messageText, String image, String profession, String email, String imageContent, String filename, String fileUrl,String uid) {
        this.name = name;
        this.messageText = messageText;
        this.image = image;
        this.profession = profession;
        this.email = email;
        this.imageContent = imageContent;
        this.filename = filename;
        this.fileUrl = fileUrl;
        this.uid = uid;
    }

    public String getUserId() {
        return userId;
    }
    public String getId() {
        return id;
    }
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String imageUrl) {
        this.image = imageUrl;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAnnouncementId() {
        return announcementId;
    }

    // Setter for announcementId
    public void setAnnouncementId(String announcementId) {
        this.announcementId = announcementId;
    }
}
