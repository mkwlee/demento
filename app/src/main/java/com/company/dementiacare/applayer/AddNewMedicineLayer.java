package com.company.dementiacare.applayer;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.company.dementiacare.UserHelper;
import com.company.dementiacare.database.AppDatabase;
import com.company.dementiacare.database.Med;

import java.util.ArrayList;
import java.util.List;


public class AddNewMedicineLayer {

    //region Insert Medicine
    public static void AddMedicine(@NonNull final AppDatabase db, int tagDaily, String medName, String dosage, String color, String unit, String patient, String type, String des,
                                   String startDate, String endDate, ArrayList<ArrayList<String>> arrTimeItem) throws  Exception {

        AddMedicine task = new AddMedicine(db, tagDaily, medName, dosage, color, unit, patient, type, des, startDate, endDate, arrTimeItem);

        //created threads so that adding up of medications can be done asynchronously.
        task.execute();
    }

    private static boolean InsertMeds(@NonNull final AppDatabase db, int tagDaily, String medName,
                                      String dosage, String color, String unit, String patient, String type, String des, String startDate,
                                      String endDate, ArrayList<ArrayList<String>> arrTimeItem) throws Exception {
        Med med = new Med();
        med.tagDaily = tagDaily;
        med.medName = medName;
        med.dosage = dosage;
        med.medUnit = unit;
        med.medType = type;
        med.medColor = color;
        med.medDes = des;
        med.patient = patient;
        med.startDate = startDate;
        med.endDate = endDate;
        med.timeObject = arrTimeItem; //arraylist of arraylist<strings>
        db.medModel().insertMeds(med);
        return true;
    }

//    //region Call out all the medication data.
//    public static List<Med> GetMedData(@NonNull final AppDatabase db) {
//        List<Med> medList = db.medModel().loadAllMeds();
//        return medList;
//    }
    //endregion


    //region Call out the emailID of the user from the user table

    public static String GetEmailId(AppDatabase appData) {
        List<UserHelper> userList = appData.userModel().loadAllUsers();
        return userList.get(0).getEmail();
    }
    //endregion

    //region ASYNC CLASS
    private static class AddMedicine extends AsyncTask<String, String, String> {

        private final AppDatabase mDb;
        private final int tagDaily;
        private final String medName, dosage, unit, color, type, patient, des, startDate,endDate ;
        private final ArrayList<ArrayList<String>> arrTimeItem;
        AddMedicine(AppDatabase db, int tagDaily, String medName,
                String dosage, String color, String unit, String patient, String type, String des, String startDate, String endDate, ArrayList<ArrayList<String>> arrTimeItem) {
            mDb = db;
            this.tagDaily = tagDaily;
            this.medName = medName;
            this.dosage = dosage;
            this.color = color;
            this.unit = unit;
            this.type = type;
            this.patient = patient;
            this.des = des;
            this.startDate= startDate;
            this.endDate= endDate;
            this.arrTimeItem= arrTimeItem;
        }

        @Override
        protected String doInBackground(final String... params) {
            try {
                InsertMeds(mDb, tagDaily, medName, dosage, color, unit, patient, type, des, startDate, endDate, arrTimeItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //endregion

}
