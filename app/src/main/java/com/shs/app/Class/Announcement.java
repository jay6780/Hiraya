package com.shs.app.Class;

import java.util.Map;

public class Announcement {
    private String id;
    private String image;
    private boolean liked;
    private String title;
    private String content;
    private String time;
    private String date;
    private String name;
    private String imageUrl;
    private String fileUrl; // Added fileUrl

    private Map<String, Boolean> likes;
    private int likesCount;

    public Announcement() {
        // Default constructor required for Firebase Realtime Database
    }

    public Announcement(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    // Getter method for the liked status
    public boolean isLiked() {
        return liked;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }
}
