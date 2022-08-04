/*
 *      Time picker fragment class
 * 
 *    Description: This class is used to control the time picker fragment dialog.
 * 
 *  Update History: July 21, 2022
*/


package com.company.dementiacare.component;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import com.company.dementiacare.R;
import com.company.dementiacare.ui.add.ReminderActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Collections;

public class TimePickerFragment extends DialogFragment {

    // call the activity that creates an instance of this dialog fragment
    private TimeEntry selectedTime;
    // position of the time entry in the list of time entries
    int position;
    // the activity that creates an instance of this dialog fragment
    private ReminderActivity addNewMedicineActivityObj;

    // set the position of the time entry in the list of time entries
    public void setPosition(final int position) {
        this.position = position;
    }

    // set the activity that creates an instance of this dialog fragment
    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    // set the time picker listener
    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                // when the time is set
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    selectedTime = new TimeEntry(hourOfDay, minute);
                    // check if this is an attempt to add time entry with existing value
                    for(TimeEntry timeEntry : addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList()) {
                        // if the time entry is already in the list of time entries
                        if(selectedTime.compareTo(timeEntry) == 0) {
                            Toast.makeText(getActivity(), "Could not add/change - time"+
                                            String.format("%02d",hourOfDay) + ":" +
                                            String.format("%02d",minute) + "is already existed!"
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    // add the time entry to the list of time entries
                    if(position >= 0) {
                        addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList().set(position, selectedTime);
                    }
                    else {
                        addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList().add(selectedTime);
                    }
                    // set the current date in the text view of the time entry
                    MaterialButton currentButton = addNewMedicineActivityObj.getCurrentDayButton();
                    if(null != currentButton) {
                        currentButton.setTextColor(
                                addNewMedicineActivityObj.getResources().getColor(R.color.green));
                    }

                    // sort the list of time entries
                    Collections.sort(addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList());
                    addNewMedicineActivityObj.getTimeListAdapter().notifyDataSetChanged();
                }
            };
}
