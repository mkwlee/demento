package com.company.dementiacare;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserHelper {
    String name, username, email, phone, password, address;

    public UserHelper(){
    }

    // constructor for signing up
    public UserHelper(String name, String username, String email, String phone, String password, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    // constructor without password
    public UserHelper(String name, String username,  String email, String phone, String address) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = "Not to be saved in database";
        this.address = address;
    }

    // constructor without username and  password
    public UserHelper(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = "Not to be saved in database";
        this.address = address;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("phone", phone);
        result.put("password", password);
        result.put("address", address);

        return result;
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

    public String getAddress() {
        return address;
    }
}
