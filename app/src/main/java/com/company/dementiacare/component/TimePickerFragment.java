package com.company.dementiacare.component;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;

import com.company.dementiacare.R;
import com.company.dementiacare.ui.add.ReminderActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Collections;

public class TimePickerFragment extends DialogFragment {

    private TimeEntry selectedTime;
    int position;
    private ReminderActivity addNewMedicineActivityObj;

    public void setPosition(final int position) {
        this.position = position;
    }

    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), timeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }


    private TimePickerDialog.OnTimeSetListener timeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    selectedTime = new TimeEntry(hourOfDay, minute);
                    // check if this is an attempt to add time entry with existing value
                    for(TimeEntry timeEntry : addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList()) {
                        if(selectedTime.compareTo(timeEntry) == 0) {
                            Toast.makeText(getActivity(), "Not added - attempt to add existing time" +
                                            String.format("%02d",hourOfDay) + ":" +
                                            String.format("%02d",minute)
                                    , Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if(position >= 0) {
                        addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList().set(position, selectedTime);
                    }
                    else {
                        addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList().add(selectedTime);
                    }
                    MaterialButton currentButton = addNewMedicineActivityObj.getCurrentDayButton();
                    if(null != currentButton) {
                        currentButton.setTextColor(
                                addNewMedicineActivityObj.getResources().getColor(R.color.teal_200));
                    }

                    Collections.sort(addNewMedicineActivityObj.getCurrentDay().getTimeEntriesList());
                    addNewMedicineActivityObj.getTimeListAdapter().notifyDataSetChanged();
                }
            };
}
