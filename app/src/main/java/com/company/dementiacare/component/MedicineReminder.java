package com.company.dementiacare.component;

import java.util.ArrayList;

public class MedicineReminder {
    static private ArrayList<ArrayList<String>> weekSchedule;

    static private String patient;
    static private String name;
    static private String dosage;
    static private String unit;
    static private String type;
    static private String color;
    static private String startDate;
    static private String endDate;
    static private String path;
    static private int isDaily;

    public MedicineReminder() {
        weekSchedule = new ArrayList<ArrayList<String>>();
    }

    public void setPatient(String patient) {this.patient = patient;}

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {this.type = type;}

    public void setColor(String color) {this.color = color;}

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setUnit(String unit){ this.unit = unit;}

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setIsDaily(int isDaily) {
        this.isDaily = isDaily;
    }

    public void setDrugBoxImagePath(String path) {
        this.path = path;
    }

    public String getPatient(){
        return this.patient;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {return this.type;}

    public String getColor() {return this.color;}

    public String getDosage() {
        return this.dosage;
    }

    public static String getUnit() {
        return unit;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public int getIsDaily() {
        return this.isDaily;
    }

    public String getDrugBoxImagePath() {
        return this.path;
    }

    public ArrayList<ArrayList<String>> getWeekSchedule() {
        return this.weekSchedule;
    }
}
