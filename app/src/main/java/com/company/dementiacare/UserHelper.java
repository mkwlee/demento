/*
 *          user data
 * 
 *  Description: This activity is used to register the user.
 * 
 * 
 * updated: July 21, 2022
*/


package com.company.dementiacare;

import com.company.dementiacare.ui.profile.ClientProfile;

public class UserHelper {
    // variables
    String name, username, email, phone, password;


    StaticRVAdapter medicines;
    ClientHelper client;

    public UserHelper(){
    }

    // constructor
    public UserHelper(String name, String username, String email, String phone, String password, ClientHelper client, StaticRVAdapter medicines) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.client = client;
        this.medicines = medicines;
    }

    public StaticRVAdapter getMedicines() {
        return medicines;
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

    public ClientHelper getClient(){
        return client;
    }
}
