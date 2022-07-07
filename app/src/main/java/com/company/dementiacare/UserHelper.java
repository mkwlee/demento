/*
 *      User Helper Class
 * 
 *   Description: This class is used to help the user to login and register.
 * 
 *  Updated:
 *  July 07, 2022
 * 
 * 
*/

package com.company.dementiacare;

public class UserHelper {
    String name, username, email, phone, password;

    public UserHelper(){
    }

    // get the user's details
    public UserHelper(String name, String username, String email, String phone, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }
}
