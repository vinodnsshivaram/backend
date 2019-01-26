package com.baagir.shopping.model;

public class User {
    private String lastName;
    private String firstName;
    private String userName;
    private String password;
    private String emailAddress;

    public User(String lastName, String firstName, String userName, String password, String emailAddress) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public User() {
        this.lastName = "lastName";
        this.firstName = "firstName";
        this.userName = "userName";
        this.password = "password";
        this.emailAddress = "emailAddress";
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
