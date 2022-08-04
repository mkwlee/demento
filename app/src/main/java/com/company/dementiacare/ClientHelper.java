/*
 *          Client's data
 *
 *  Description: A model of client
 *
 *
 * updated: August 3rd 2022
 */
package com.company.dementiacare;

public class ClientHelper {

    String name;
    String age;
    String gender;
    String height;
    String weight;
    String stage;

    public ClientHelper() {
        this.name = "";
        this.age = "";
        this.gender = "";
        this.height = "";
        this.weight = "";
        this.stage = "";
    }

    public ClientHelper(String name, String age, String gender, String height, String weight, String stage) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.stage = stage;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

    public String getStage() {
        return stage;
    }


}
