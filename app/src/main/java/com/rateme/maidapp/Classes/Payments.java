package com.rateme.maidapp.Classes;

public class Payments {
    String sender, receiver;

    public Payments(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Payments() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
