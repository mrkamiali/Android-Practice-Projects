package com.kamranali.firebasemodel.model;

/**
 * Created by kamranali on 21/02/2017.
 */
public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String userID;
    private String password;

    public User(String email, String userID, String firstName, String lastName,String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
