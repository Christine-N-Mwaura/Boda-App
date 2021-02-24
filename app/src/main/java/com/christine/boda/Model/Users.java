package com.christine.boda.Model;

public class Users {
    String EmailAddress, FirstName, LastName, Password, Phone;

    public Users(String emailAddress, String firstName, String lastName, String password, String phone) {
        EmailAddress = emailAddress;
        FirstName = firstName;
        LastName = lastName;
        Password = password;
        Phone = phone;
    }

    public Users() {
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}