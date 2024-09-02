package com.example.ex06;

public class ChatVO {
    private String key;
    private String email;
    private String date;
    private String contents;

    public String getKey() {
        return key;
    }
    public String getEmail() {
        return email;
    }
    public String getDate() {
        return date;
    }
    public String getContents() {
        return contents;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "ChatVO{" +
                "key='" + key + '\'' +
                ", email='" + email + '\'' +
                ", date='" + date + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
