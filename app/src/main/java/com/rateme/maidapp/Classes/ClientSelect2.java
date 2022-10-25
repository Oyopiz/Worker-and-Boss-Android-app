package com.rateme.maidapp.Classes;

public class ClientSelect2 {
    String name,uid,phone,experience,id,age;

    public ClientSelect2(String name, String uid, String phone, String experience, String id, String age) {
        this.name = name;
        this.uid = uid;
        this.phone = phone;
        this.experience = experience;
        this.id = id;
        this.age = age;
    }

    public ClientSelect2() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
