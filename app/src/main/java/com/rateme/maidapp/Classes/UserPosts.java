package com.rateme.maidapp.Classes;

public class UserPosts {
    String name, id, age, experience, uid, phone, coordninates, witnname, witcontact;


    public UserPosts(String name, String id, String age, String experience, String uid, String phone, String coordninates, String witnname, String witcontact) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.experience = experience;
        this.uid = uid;
        this.phone = phone;
        this.coordninates = coordninates;
        this.witnname = witnname;
        this.witcontact = witcontact;
    }

    public UserPosts() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
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

    public String getCoordninates() {
        return coordninates;
    }

    public void setCoordninates(String coordninates) {
        this.coordninates = coordninates;
    }

    public String getWitnname() {
        return witnname;
    }

    public void setWitnname(String witnname) {
        this.witnname = witnname;
    }

    public String getWitcontact() {
        return witcontact;
    }

    public void setWitcontact(String witcontact) {
        this.witcontact = witcontact;
    }
}
