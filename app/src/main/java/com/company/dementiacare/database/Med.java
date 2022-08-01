package com.company.dementiacare.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Med {

    @PrimaryKey(autoGenerate = true)@NonNull
    public int id;
    public int tagDaily; //this flag is for tagging daily meds.
    public String medName;
    public String medType;
    public String medColor;
    public String dosage;
    public String medUnit;
    public String medDes;
    public String patient;
    public String startDate;
    public String endDate;
    public ArrayList<ArrayList<String>> timeObject = new ArrayList<ArrayList<String>>();
    public Med() {
    }

    public Med(int id,int tagDaily, String medName, String dosage,String medColor, String medUnit, String patient, String medType, String medDes,String startDate,String endDate,ArrayList<ArrayList<String>> arrTimeItem){
        this.id = id;
        this.tagDaily = tagDaily;
        this.medName= medName;
        this.dosage= dosage;
        this.startDate= startDate;
        this.endDate= endDate;
        this.timeObject.addAll(arrTimeItem);
        this.medColor = medColor;
        this.medDes = medDes;
        this.medType = medType;
        this.medUnit = medUnit;
        this.patient = patient;
    };
}
