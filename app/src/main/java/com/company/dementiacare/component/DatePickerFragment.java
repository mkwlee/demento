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
    // source : http://www.zoftino.com/android-timepicker-example

    ReminderActivity addNewMedicineActivityObj;

    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH);
        int day = c.get(c.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }


    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String date = String.format("%02d",month+1) + "/" +
                            String.format("%02d",day) + "/" + String.format("%04d",year);
                    addNewMedicineActivityObj.setDateToMedicineSchedule(date);
                }
            };
}
