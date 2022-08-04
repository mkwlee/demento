package com.company.dementiacare;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

// modal for the static reminder list
public class StaticRVModel {

    // variables
    private int image;
    private int tagDaily;
    private int cardColor;
    private String name;
    private String time;
    private String dosage;
    private String unit;
    private String color;
    private String type;
    private String description;
    boolean visibility;
    private String patient;
//    private boolean isDateVisible;

    private ArrayList<ArrayList<String>> arrTime;

    public StaticRVModel(){
    }

    // constructor
    public StaticRVModel(int image, int tagDaily, int cardColor, String patient, String name, String time, String description, String dosage, String color, String type, String unit, ArrayList<ArrayList<String>> arrTime) {
        this.image = image;
        this.tagDaily = tagDaily;
        this.name = name;
        this.time = time;
        this.description = description;
        this.visibility = false;
        this.dosage = dosage;
        this.color = color;
        this.type = type;
        this.arrTime = arrTime;
        this.unit = unit;
        this.cardColor = cardColor;
        this.patient = patient;
//        this.isDateVisible = false;
    }
    
    // get the image
    public int getImage() {
        return image;
    }

    public int getTagDaily() {
        return tagDaily;
    }

    // get the name
    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDosage() {
        return dosage;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public String getPatient() {return patient;}

    public ArrayList<ArrayList<String>> getArrTime() {
        return arrTime;
    }

    public String getDescription() {
        return description;
    }

    // get the visibility
    public boolean isVisible(){
        return visibility;
    }

    // set the image based on the type
    public void setImage(int image) {
        this.image = image;
    }

    public int getCardColor() {
        return cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setPatient(String patient) {this.patient = patient;}

//    public void setDateVisible(boolean dateVisible) {this.isDateVisible = dateVisible;}

    // set the visibility
    public void setVisibility(boolean visibility){
        this.visibility = visibility;
    }
}
