package com.rateme.maidapp.Classes;

public class UserHelper {
    String url, username, email, location, uid,role;

    public UserHelper(String url, String username, String email, String location, String uid, String role) {
        this.url = url;
        this.username = username;
        this.email = email;
        this.location = location;
        this.uid = uid;
        this.role = role;
    }

    public UserHelper() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
