package com.rateme.maidapp.Classes;

public class Shortlist {
    String email, state, uid,name;

    public Shortlist(String email, String state, String uid, String name) {
        this.email = email;
        this.state = state;
        this.uid = uid;
        this.name = name;
    }

    public Shortlist() {
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
