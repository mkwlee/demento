/*
 *          user data
 *
 *  Description: This activity is used to register the user.
 *
 *
 * updated: July 21, 2022
 */


package com.company.dementiacare;

import java.util.ArrayList;

public class UserHelper {
    // variables
    String name, username, email, phone, password;
    ArrayList<ClientHelper> client;
    StaticRVAdapter medicines;
    public UserHelper(){
    }

    // constructor
    public UserHelper(String name, String username, String email, String phone, String password, ArrayList<ClientHelper> client, StaticRVAdapter medicines) {
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

    public ArrayList<ClientHelper> getClient(){
        return client;
    }
    public void setClient(ArrayList<ClientHelper> client) {
        this.client = client;
    }

}
