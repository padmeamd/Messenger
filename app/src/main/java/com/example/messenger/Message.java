package com.example.messenger;

public class Message {
    public String text, sender, profile_pic_URL;

    public Message( String sender, String text, String profile_pic_URL) {
        this.text = text;
        this.sender = sender;
        this.profile_pic_URL = profile_pic_URL;
    }

    public Message() {
    }
}
