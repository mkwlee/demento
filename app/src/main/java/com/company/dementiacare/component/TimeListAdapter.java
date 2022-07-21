/*
 *      Time List Adapter Class
 * 
 *  Description: This class is used to display the list of times in the TimeListFragment.
 * 
 *  Update History: July 21, 2022
*/


package com.company.dementiacare.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.dementiacare.R;
import com.company.dementiacare.ui.add.ReminderActivity;

import java.util.ArrayList;
import java.util.Collections;

public class TimeListAdapter extends ArrayAdapter<TimeEntry> {

    // create an array list to store the time entries
    private ArrayList<TimeEntry> timeEntriesList;
    // call the activity that creates an instance of this adapter
    private ReminderActivity addNewMedicineActivityObj;

    // constructor
    public TimeListAdapter(Context context, int resource, ArrayList<TimeEntry> timeEntries) {
        super(context, resource, timeEntries);
        this.timeEntriesList = timeEntries;
    }

    // set the activity that creates an instance of this adapter
    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    // get the time entries
    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        // if the view is null, inflate the view
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.frequency_entry, null);

            // find the remove button and set the listener
            ImageButton removeTimeButton = view.findViewById(R.id.removeTime_button);
            // set the on click listener for the remove time button
            removeTimeButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get the position of the time entry
                    View parentRow = (View) view.getParent();
                    // get the parent list view
                    ListView listView = (ListView) parentRow.getParent();
                    // get the position of the time entry in the list view
                    final int finalPosition = listView.getPositionForView(parentRow);

                    // check if the time entry is not null then remove it from the list
                    if(null != timeEntriesList.get(finalPosition)) {
                        timeEntriesList.remove(finalPosition);
                        // subtract 1 the add days int
                        addNewMedicineActivityObj.subtractAddDays();
                        // notify the adapter that the data has changed
                        if((0 == timeEntriesList.size()) &&
                                (null != addNewMedicineActivityObj.getCurrentDayButton())) {
                            addNewMedicineActivityObj.getCurrentDayButton().setTextColor(
                                    addNewMedicineActivityObj.getResources().getColor(R.color.black));
                        }
                        // notify the adapter that the data has changed
                        Collections.sort(timeEntriesList);
                        TimeListAdapter.this.notifyDataSetChanged();
                    }

                }
            });

            // find the edit button
            ImageButton editTimeButton = view.findViewById(R.id.editTime_button);
            // set the on click listener for the edit time button
            editTimeButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // get the position of the time entry
                    View parentRow = (View) view.getParent();
                    // get the parent list view
                    ListView listView = (ListView) parentRow.getParent();
                    // get the position of the time entry in the list view
                    final int finalPosition = listView.getPositionForView(parentRow);
                    // set the time picker dialog
                    addNewMedicineActivityObj.setTimePickerFragment(finalPosition);
                }
            });
        }

        // get the time entry
        TimeEntry timeEntry = timeEntriesList.get(position);
        // if the time entry is not null, set the time entry
        if(null != timeEntry) {
            TextView time = view.findViewById(R.id.timeText_view);
            time.setText(String.format("%02d",timeEntry.getHour()) + ":" +
                    String.format("%02d",timeEntry.getMinute()));
        }
        // return the view of the time entry
        return view;
    }
}

