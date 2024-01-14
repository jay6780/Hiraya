package com.shs.app.Class.comment;
public class Comment2 {
    private String name;
    private String messageText;
    private String image;
    private String profession;
    private String email;
    private String imageContent;

    // Empty constructor (required for Firebase)
    public Comment2() {
    }

    public Comment2(String name, String messageText, String image, String profession, String email,String imageContent) {
        this.name = name;
        this.messageText = messageText;
        this.image = image;
        this.profession = profession;
        this.email = email;
        this.imageContent = imageContent;
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
}