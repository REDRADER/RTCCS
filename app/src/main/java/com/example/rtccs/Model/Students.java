package com.example.rtccs.Model;

public class Students {

    String fullname,email,phone,image,year,password,status;

    public Students()
    {

    }

    public Students(String fullname, String email, String phone, String image, String year, String password,String status) {
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.year = year;
        this.password = password;
        this.status = status;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
