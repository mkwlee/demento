/*
 *          user data
 * 
 *  Description: This activity is used to register the user.
 * 
 * 
 * updated: July 21, 2022
*/


package com.company.dementiacare;

public class UserHelper {
    // variables
    String name, username, email, phone, password;

    public UserHelper(){
    }

    // constructor
    public UserHelper(String name, String username, String email, String phone, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // get the name
    public String getName() {
        return name;
    }

    // get the username
    public String getUsername() {
        return username;
    }

    // get the email
    public String getEmail() {
        return email;
    }

    // get the phone
    public String getPhone() {
        return phone;
    }

    // get the password
    public String getPassword() {
        return password;
    }
}
