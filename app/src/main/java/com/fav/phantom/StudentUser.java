package com.fav.phantom;

public class StudentUser {
    private String email;
    private String userName;
    private String registrationCode;

    public String getEmail() {
        return email;
    }

    public StudentUser(){

    }

    public StudentUser(String email, String userName, String registrationCode) {
        this.email = email;

        this.userName = userName;
        this.registrationCode = registrationCode;
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

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }
}
