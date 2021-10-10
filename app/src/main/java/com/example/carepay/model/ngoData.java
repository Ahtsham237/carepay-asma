package com.example.carepay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ngoData {
    @SerializedName("uid")
    @Expose
    String uid;
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("detail")
    @Expose
    String detail;
    @SerializedName("type")
    @Expose
    String type;
    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("link")
    @Expose
    String link;
    @SerializedName("userimage")
    @Expose
    String userimage;
    @SerializedName("username")
    @Expose
    String username;
    String filename;
    String key;
    String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
