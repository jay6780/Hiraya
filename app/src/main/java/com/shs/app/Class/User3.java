package com.shs.app.Class;

public class User3 {
    private Long age; // Change the data type to Long
    private String email;
    private String gender;
    private String image;
    private String name;
    private String username;
    private String phone;// Added phone field
    private String birthday;

    public User3() {
        // Default constructor required for Firebase
    }

    public User3(Long age, String email, String gender, String image, String name, String username,
                 String phone, String birthday) {
        this.age = age;
        this.email = email;
        this.gender = gender;
        this.image = image;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.birthday = birthday;

    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    // Create getters and setters for your fields
    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
