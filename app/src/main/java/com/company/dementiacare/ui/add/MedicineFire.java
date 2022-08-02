package com.company.dementiacare.ui.add;

public class MedicineFire {

    private final String medicineName;
    private final String medicineDosage;
    private final String medicineColor;
    private final String medicineUnit;
    private final String medicinePatient;
    private final String medicineType;
    private final String medicineDes;
    private final String dateTimeRegistered;

    public MedicineFire(){
        this.medicineName="";
        this.medicineDosage="";
        this.medicineColor="";
        this.medicineUnit="";
        this.medicinePatient="";
        this.medicineType="";
        this.medicineDes="";
        this.dateTimeRegistered="";
    }

    public MedicineFire(String medicineName, String medicineDosage, String medicineColor, String medicineUnit, String medicinePatient, String medicineType, String medicineDes, String dateTimeRegistered) {
        this.medicineName = medicineName;
        this.medicineDosage = medicineDosage;
        this.medicineColor = medicineColor;
        this.medicineUnit = medicineUnit;
        this.medicinePatient = medicinePatient;
        this.medicineType = medicineType;
        this.medicineDes = medicineDes;
        this.dateTimeRegistered = dateTimeRegistered;
    }


    public String getMedicineName() {
        return medicineName;
    }

    public String getMedicineDosage() {
        return medicineDosage;
    }

    public String getMedicineColor() {
        return medicineColor;
    }

    public String getMedicineUnit() {
        return medicineUnit;
    }

    public String getMedicinePatient() {
        return medicinePatient;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public String getMedicineDes() {
        return medicineDes;
    }

    public String getDateTimeRegistered() {
        return dateTimeRegistered;
    }


}
