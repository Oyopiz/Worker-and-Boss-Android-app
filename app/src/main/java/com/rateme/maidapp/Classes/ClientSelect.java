package com.rateme.maidapp.Classes;

public class ClientSelect {
    String uid, email, state,name;

    public ClientSelect(String uid, String email, String state, String name) {
        this.uid = uid;
        this.email = email;
        this.state = state;
        this.name = name;
    }

    public ClientSelect() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
