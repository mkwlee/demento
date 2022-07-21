
/*
 *      ReminderActivity class
 * 
 *  Description: This class is used to create a reminder for the user.
 * 
 *  updated: July 21, 2022
*/

package com.company.dementiacare.ui.add;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dementiacare.MainActivity;
import com.company.dementiacare.R;
import com.company.dementiacare.component.DatePickerFragment;
import com.company.dementiacare.component.TimeListAdapter;
import com.company.dementiacare.component.TimePickerFragment;
import com.company.dementiacare.component.WeekDay;
import com.company.dementiacare.ui.home.Homepage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class ReminderActivity extends AppCompatActivity{

    // Variables
    
    // text input fields
    private TextInputEditText startDate, endDate;

    // call the time picker fragment
    private TimePickerFragment timePickerFragment;
    // call the date picker fragment 
    private DatePickerFragment datePickerFragment;

    // list view for the date buttons
    public static MaterialButton[] weekDayButtons = new MaterialButton[7];
    // number of days selected
    static int countDays = 0;
    // list of days added
    static int addDays = 0;

    // list of days
    private final String[] weekDaysArr = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};
    
    // make an array list of times
    public ArrayList<String> timeEntriesStrings;
    // call the time list adapter
    private TimeListAdapter timeListAdapter;

    // hash map of week days and their corresponding index
    private HashMap<String, WeekDay> buttonIdStrToWeekDayMap = new HashMap<>();
    // call the week day class
    private WeekDay currentDay;
    // button
    private MaterialButton currentDayButton;
    //a boolean to check if the day is selected or not
    boolean isSettingStartDate;

    // relative layout for the days
    private RelativeLayout daysRelativeLayout;
    // linear layout for the duration, schedule and save button
    private LinearLayout durationLayout, scheduleLayout, saveButton;

    // radio group for the schedule
    private RadioGroup scheduleRadioGroup;
    private ImageButton removeButton, addButton, backButton;
    private TextView dayTimesText, timesText, daysText;

    private MaterialButton recurringButton, oneTimeButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
//        dayTimesText = findViewById(R.id.dayTimes_text);
//        dayTimesText.setText("Please add frequency");
//        timesText = findViewById(R.id.times_text);
//        timesText.setVisibility(View.GONE);
//        daysText = findViewById(R.id.day_text);
//        daysText.setVisibility(View.GONE);
//        saveButton = findViewById(R.id.save_button);

        // set the window to full screen
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        // find the back button
        backButton = findViewById(R.id.back_to_medicine);
        // back to the medicine activity when clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // find the layout of the days
        daysRelativeLayout = findViewById(R.id.days_relativeLayout);
        // set it to GONE when specific is not selected
        daysRelativeLayout.setVisibility(View.GONE);

        // find the reminders buttons
        recurringButton = findViewById(R.id.recurring_button);
        oneTimeButton = findViewById(R.id.oneTime_button);

        // if the recurring button is clicked, show the days layout
        recurringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableOneTime(false);
                // change the button color
                recurringButton.setBackgroundTintList(getResources().getColorStateList(R.color.teal_200));
                oneTimeButton.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue_button));
            }
        });
        // disable the schedule and the duration section when one time and change the button
        oneTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableOneTime(true);
                // change the button color
                oneTimeButton.setBackgroundTintList(getResources().getColorStateList(R.color.teal_200));
                recurringButton.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue_button));
            }
        });

        // find the start and end dates input
        startDate = findViewById(R.id.startDate_text);
        endDate = findViewById(R.id.endDate_text);

        // functions to call the calendar fragment when input are clicked
        startDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (buttonNotInFocus(view, event)) {
                            return false;
                        }
                        ReminderActivity.this.isSettingStartDate = true;
                        setDatePickerFragment();
                        return false;
                }
                return false;
            }
        });
        // functions to call the calendar fragment when input are clicked
        endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (buttonNotInFocus(view, event)) {
                            return false;
                        }
                        ReminderActivity.this.isSettingStartDate = false;
                        setDatePickerFragment();
                        return false;
                }
                return false;
            }
        });

        // find the week day ids from the week day list and add them to the hash map
        for (int weekDayIndex = 0; weekDayIndex < weekDaysArr.length; ++weekDayIndex) {
            int weekDayId = getResources().getIdentifier(weekDaysArr[weekDayIndex], "id",
                    getApplicationContext().getPackageName());
            buttonIdStrToWeekDayMap.put(Integer.toString(weekDayId), new WeekDay(weekDaysArr[weekDayIndex]));

            // find the week day buttons and add them to the list
            final MaterialButton currentDayButton = findViewById(weekDayId);
            weekDayButtons[weekDayIndex] = currentDayButton;
            // set a listener to the buttons
            currentDayButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            return false;
                        case MotionEvent.ACTION_UP:
                            if (buttonNotInFocus(view, event)) {
                                return false;
                            }
                            // set the current day
                            setCurrentDayButtonSelected(currentDayButton);
                            ReminderActivity.this.currentDayButton = currentDayButton;
                            // Add Time button should be active only when week mode selected
                            // or specific day selected
                            int buttonId = currentDayButton.getId();
                            currentDay = buttonIdStrToWeekDayMap.get(Integer.toString(buttonId));
                            setListViewAdapter(currentDay);
                            return false;
                    }
                    return false;
                }
            });
        }

        // find the schedule radio group by id
        scheduleRadioGroup = findViewById(R.id.schedule_radioGroup);
        // check if the radio button is selected show the days layout
        scheduleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // if the week mode is selected, show the days layout
                currentDay = new WeekDay("AllDays");
                // set the days layout to visible
                if (i == R.id.daily_button) {
                    // clear the list view
                    clearCurrentSchedule();
                    // set the days layout to not visible
                    enableDays(false);
                    // set the current day to all days to null
                    ReminderActivity.this.currentDayButton = null;
                    // set the list adapter to the current day
                    setListViewAdapter(currentDay);
                } else {
                    // clear the list view
                    clearCurrentSchedule();
                    // set the days layout to visible
                   enableDays(true);
                   // set the list adapter to the current day
                   setListViewAdapter(currentDay);
                }
            }
        });

        // find the add and remove buttons
        addButton = findViewById(R.id.increase_button);

        // set the on click listener for the add button to add a day to the days layout
        addButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return false;
                    case MotionEvent.ACTION_UP:
                        if (buttonNotInFocus(view, event)) {
                            return false;
                        }
                        // show the times picker layout
                        setTimePickerFragment(-1);
//                        addDay();
                        return false;
                }
                return false;
            }
        });

        // find the save button
        saveButton = findViewById(R.id.save_button);
        
        // when the save button is clicked, send a tost to the user
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make a toast and let them know this is for sprint 3!
                Toast.makeText(getApplicationContext(), "Save function will be implemented in Sprint 3", Toast.LENGTH_LONG).show();

                // navigate to the main activity
                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                startActivity(intent);
            }
        });

    }

    // when back button is pressed to animation for navigation
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // setters
    // set the data to medication object
    public void setDateToMedicineSchedule(String date) {
        if (isSettingStartDate) {
            startDate.setText(date);
        } else {
            endDate.setText(date);
        }
    }

    // set the time picker fragment
    public void setTimePickerFragment(final int position) {
        timePickerFragment = new TimePickerFragment();
        timePickerFragment.setPosition(position);
        timePickerFragment.setAddNewMedicineActivityObj(this);
        timePickerFragment.show(getSupportFragmentManager(), "time picker");
    }
    // set the date picker fragment
    public void setDatePickerFragment() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setAddNewMedicineActivityObj(this);
        datePickerFragment.show(getSupportFragmentManager(), "date picker");
    }

    // set the list view adapter
    private void setListViewAdapter(WeekDay currentDay) {
        // set the list view adapter to the layout list view
        timeListAdapter = new TimeListAdapter(ReminderActivity.this,
                R.layout.frequency_entry, currentDay.getTimeEntriesList());
        timeListAdapter.setAddNewMedicineActivityObj(this);

        // set the list view adapter to the layout list view
        ListView timeEntriesListView = findViewById(R.id.timeEntriesListView);
        timeEntriesListView.setAdapter(timeListAdapter);
    }

    // enable the days layout if the week mode is selected
    private void enableDays(boolean isEnabled){
        // set the days layout to visible and color
        for (MaterialButton weekDayButton : weekDayButtons) {
            weekDayButton.setEnabled(isEnabled);
            if (isEnabled) {
                weekDayButton.setTextColor(getResources().getColor(R.color.black));
            }
        }

        // set the days layout to not visible and color
        daysRelativeLayout = findViewById(R.id.days_relativeLayout);
        if (isEnabled){
            daysRelativeLayout.setVisibility(View.VISIBLE);
        }
        else{
            daysRelativeLayout.setVisibility(View.GONE);
        }
        // set the days layout to not visible and color
        setCurrentDayButtonSelected(null);
    }

    // enable the onetime layout if the onetime mode is selected
    private void enableOneTime(boolean isOneTime){

        // find the duration and schedule layout for visibility
        durationLayout = findViewById(R.id.duration_section);
        scheduleLayout = findViewById(R.id.schedule_section);

        // set the duration and schedule layout to not visible and color
        if (isOneTime){
            durationLayout.setVisibility(View.GONE);
            scheduleLayout.setVisibility(View.GONE);
        }
        // set the duration and schedule layout to visible and color
        else{
            durationLayout.setVisibility(View.VISIBLE);
            scheduleLayout.setVisibility(View.VISIBLE);
        }
    }

    // clear the list view
    private void clearCurrentSchedule() {
        for (WeekDay weekDay : buttonIdStrToWeekDayMap.values()) {
            weekDay.getTimeEntriesList().clear();
        }

        // clear current day - useful when switching from week schedule to daily schedule
        if (null != currentDay) {
            currentDay.getTimeEntriesList().clear();
        }
    }

    // Getters

    // get the added day
    public int getAddDays(){
        return  this.addDays;
    }

    // get the current day button
    public MaterialButton getCurrentDayButton() {
        return this.currentDayButton;
    }

    // get the current day
    public WeekDay getCurrentDay() {
        return this.currentDay;
    }

    // get the time list adapter
    public TimeListAdapter getTimeListAdapter() {
        return this.timeListAdapter;
    }

    // check if any days are selected
    public boolean allDaysAreEmpty() {
        for (WeekDay weekDay : buttonIdStrToWeekDayMap.values()) {
            if (weekDay.getTimeEntriesList().size() > 0) {
                return false;
            }
        }
        return true;
    }

    // set the current day button selected
    private void setCurrentDayButtonSelected(MaterialButton currentDayButton) {
        for (MaterialButton weekDayButton : weekDayButtons) {
            if (weekDayButton == currentDayButton) {
                weekDayButton.setBackgroundResource(R.drawable.selected_day_button);
            } else {
                weekDayButton.setBackgroundResource(R.drawable.day_button);
            }
        }
    }

    // custom toast
    private Toast getToastDialog() {
        // create a toast
        Toast toast = Toast.makeText(ReminderActivity.this, ""
                , Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        // set the toast background color
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        return toast;
    }

    // check if the button is selected
    private boolean buttonNotInFocus(View view, MotionEvent event) {
        Rect rect = new Rect();
        // get the View's (Button's) Rect relative to its parent
        view.getHitRect(rect);
        // offset the touch coordinates with the values from r
        // to obtain meaningful coordinates
        final float x = event.getX() + rect.left;
        final float y = event.getY() + rect.top;
        if (!rect.contains((int) x, (int) y)) {
            return true;
        }
        return false;
    }

//    private void addDay(){
//        if (addDays < 7){
//            dayTimesText = findViewById(R.id.dayTimes_text);
//            daysText = findViewById(R.id.day_text);
//            timesText = findViewById(R.id.day_text);
//            daysText.setVisibility(View.VISIBLE);
//            timesText.setVisibility(View.VISIBLE);
//            addDays++;
//            // convert the countDays to a string
//            String countDaysStr = String.valueOf(addDays);
//            dayTimesText.setText(countDaysStr);
//            // set time to times when day is greater than 1
//            if (addDays > 1){
//                timesText = findViewById(R.id.times_text);
//                timesText.setText(" times");
//            }
//            else {
//                timesText = findViewById(R.id.times_text);
//                timesText.setText(" time");
//            }
//        }
//    }
//  // remove day
    public void subtractAddDays() {
        addDays--;
    }
}