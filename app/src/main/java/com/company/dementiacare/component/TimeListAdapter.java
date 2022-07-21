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

    private ArrayList<TimeEntry> timeEntriesList;
    private ReminderActivity addNewMedicineActivityObj;

    public TimeListAdapter(Context context, int resource, ArrayList<TimeEntry> timeEntries) {
        super(context, resource, timeEntries);
        this.timeEntriesList = timeEntries;
    }

    public void setAddNewMedicineActivityObj(ReminderActivity addNewMedicineActivityObj) {
        this.addNewMedicineActivityObj = addNewMedicineActivityObj;
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.frequency_entry, null);

            ImageButton removeTimeButton = view.findViewById(R.id.removeTime_button);
            // source : https://jmsliu.com/2444/click-button-in-listview-and-get-item-position.html
            removeTimeButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View parentRow = (View) view.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int finalPosition = listView.getPositionForView(parentRow);
                    if(null != timeEntriesList.get(finalPosition)) {
                        timeEntriesList.remove(finalPosition);
                        // subtract 1 the add days int
                        addNewMedicineActivityObj.subtractAddDays();
                        if((0 == timeEntriesList.size()) &&
                                (null != addNewMedicineActivityObj.getCurrentDayButton())) {
                            addNewMedicineActivityObj.getCurrentDayButton().setTextColor(
                                    addNewMedicineActivityObj.getResources().getColor(R.color.black));
                        }
                        Collections.sort(timeEntriesList);
                        TimeListAdapter.this.notifyDataSetChanged();
                    }

                }
            });

            ImageButton editTimeButton = view.findViewById(R.id.editTime_button);
            editTimeButton.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View parentRow = (View) view.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int finalPosition = listView.getPositionForView(parentRow);
                    addNewMedicineActivityObj.setTimePickerFragment(finalPosition);
                }
            });
        }

        TimeEntry timeEntry = timeEntriesList.get(position);

        if(null != timeEntry) {
            TextView time = view.findViewById(R.id.timeText_view);
            time.setText(String.format("%02d",timeEntry.getHour()) + ":" +
                    String.format("%02d",timeEntry.getMinute()));
        }
        return view;
    }
}

