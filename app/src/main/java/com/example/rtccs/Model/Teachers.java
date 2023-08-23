package com.example.rtccs.Model;

public class Teachers {
    String name,image,email,phone,subject,key;

    public Teachers() {
    }

    public Teachers(String name, String image, String email, String phone, String subject, String key) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.subject = subject;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
