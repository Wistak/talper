package com.example.talper;

public class UsersItem {

    String userID;
    String userName;
    String userPassword;
    String userPuesto;

    public UsersItem() {
    }

    public UsersItem(String userID, String userName, String userPassword, String userPuesto) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userPuesto = userPuesto;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserPuesto() {
        return userPuesto;
    }

    public void setUserPuesto(String userPuesto) {
        this.userPuesto = userPuesto;
    }
}