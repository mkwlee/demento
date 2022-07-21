/*
 * 
 *          Date Picker Fragment component
 * 
 *   Description: This class is used to control the date picker fragment dialog.
 * 
 *  Update History: July 21, 2022
*/


package com.company.dementiacare.component;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.company.dementiacare.ui.add.ReminderActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    // The activity that creates an instance of this dialog fragment must
    // implement this interface in order to receive event callbacks.
    ReminderActivity addNewMedicineActivityObj;

    // set the activity that creates an instance of this dialog fragment
    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);
        int day = c.get(c.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    // set the date picker listener
    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                // when the date is set
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    // set the date in the reminder activity in a format that can be used by the database
                    String date = String.format("%02d",month+1) + "/" +
                            String.format("%02d",day) + "/" + String.format("%04d",year);
                    addNewMedicineActivityObj.setDateToMedicineSchedule(date);
                }
            };
}

