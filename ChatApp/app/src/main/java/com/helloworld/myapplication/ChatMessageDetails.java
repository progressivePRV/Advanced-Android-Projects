package com.helloworld.myapplication;

import java.util.Date;

public class ChatMessageDetails {
    String Uid;
    String firstname;
    String Message;
    Date date;
    boolean isLiked;

    public String getUid() {
        return Uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMessage() {
        return Message;
    }

    public Date getDate() {
        return date;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public String toString() {
        return "ChatMessageDetails{" +
                "Uid='" + Uid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", Message='" + Message + '\'' +
                ", date=" + date +
                ", isLiked=" + isLiked +
                '}';
    }
}
