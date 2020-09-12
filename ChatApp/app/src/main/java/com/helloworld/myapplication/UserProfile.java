package com.helloworld.myapplication;

import java.io.Serializable;

public class UserProfile implements Serializable {
    String firstName;
    String uid;
    String lastName;
    String gender;
    String city;
    String email;
    String profileImage;

    public UserProfile(String firstName, String lastName, String gender, String email, String city, String profileImage, String uid) {
        this.firstName = firstName;
        this.uid = uid;
        this.lastName = lastName;
        this.gender = gender;
        this.city = city;
        this.email = email;
        this.profileImage = profileImage;
    }
}
