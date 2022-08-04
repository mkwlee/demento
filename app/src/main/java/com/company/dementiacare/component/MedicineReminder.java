package com.company.dementiacare.component;

import com.company.dementiacare.ui.add.ReminderActivity;

import java.util.ArrayList;

public class MedicineReminder {
    static private ArrayList<ArrayList<String>> weekSchedule;

    static private String patient;
    static private String name;
    static private String dosage;
    static private String unit;
    static private String type;
    static private String color;
    static private String des;
    static private String startDate;
    static private String endDate;
    static private int isDaily;
    static private int image;
    static private int cardColor;
    ReminderActivity reminderActivity = new ReminderActivity();

    public MedicineReminder() {
        weekSchedule = new ArrayList<ArrayList<String>>();
        ArrayList<String> arr = new ArrayList<String>();
        arr.add("default");
        for (int i = 0; i < 7; i++){
            weekSchedule.add(arr);
        }
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

    public void setDes(String des){this.des = des;}

    public void setImage(int image){this.image = image;}

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setIsDaily(int isDaily) {
        this.isDaily = isDaily;
    }

    public String getPatient(){
        return this.patient;
    }

    public String getDes() {return this.des;}

    public String getName() {
        return this.name;
    }

    public String getType() {return this.type;}

    public String getColor() {return this.color;}

    public String getDosage() {
        return this.dosage;
    }

    public String getUnit() {
        return this.unit;
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

    public int getImage() {return this.image;}

    public int getCardColor() {
        return this.cardColor;
    }

    public void setCardColor(int cardColor) {
        this.cardColor = cardColor;
    }
    
    public ArrayList<ArrayList<String>> getWeekSchedule() {
        return this.weekSchedule;
    }
}
