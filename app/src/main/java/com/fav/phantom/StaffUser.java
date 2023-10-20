package com.fav.phantom;


public class StaffUser {

    private String email;
    private String userName;
    private String role;

    public StaffUser(String email, String userName, String role) {
        this.email = email;
        this.userName = userName;
        this.role = role;
    }

    public StaffUser(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

