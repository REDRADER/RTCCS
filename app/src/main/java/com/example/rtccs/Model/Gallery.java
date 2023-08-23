package com.example.rtccs.Model;

public class Gallery {
    String caption,image,data,time,key;

    public Gallery() {
    }

    public Gallery(String caption, String image, String data, String time, String key) {
        this.caption = caption;
        this.image = image;
        this.data = data;
        this.time = time;
        this.key = key;
    }


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
