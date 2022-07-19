package com.company.dementiacare.ui.add;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dementiacare.R;
import com.company.dementiacare.component.DatePickerFragment;
import com.company.dementiacare.component.WeekDay;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ReminderActivity extends AppCompatActivity implements DatePickerFragment.DatePickerListener{

    private TextInputEditText startDate, endDate;
    private DatePickerFragment datePickerFragment;
    private boolean isSettingStartDate;
    public static MaterialButton[] weekDayButtons = new MaterialButton[7];
    private final String[] weekDaysArr = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};
    static int countDays = 1;
    private HashMap<String, WeekDay> buttonIdStrToWeekDayMap = new HashMap<>();

    private WeekDay currentDay;

    private RelativeLayout daysRelativeLayout;
    private LinearLayout durationLayout, scheduleLayout;

    private int selectedScheduleInt;
    private RadioGroup scheduleRadioGroup;
    private RadioButton selectedSchedule;

    private ImageButton removeButton, addButton;
    private TextView daysText, timesText;

    private Button recurringButton, oneTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        daysRelativeLayout = findViewById(R.id.days_relativeLayout);
        daysRelativeLayout.setVisibility(View.GONE);

        // find the reminders buttons
        recurringButton = findViewById(R.id.recurring_button);
        oneTimeButton = findViewById(R.id.oneTime_button);

        // if the recurring button is clicked, show the days layout
        recurringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableOneTime(false);
            }
        });
        oneTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableOneTime(true);
            }
        });

        // find the start and end dates input
        startDate = findViewById(R.id.startDate_text);
        endDate = findViewById(R.id.endDate_text);

        // functions to call the calendar fragment when input are clicked
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReminderActivity.this.isSettingStartDate = true;
                setDatePickerFragment();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReminderActivity.this.isSettingStartDate = false;
                setDatePickerFragment();
            }
        });

        // find the schedule radio group by id
        scheduleRadioGroup = findViewById(R.id.schedule_radioGroup);
        
        // check if the radio button is selected show the days layout
        scheduleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.daily_button) {
                    enableDays(false);
                } else {
                   enableDays(true);
                }
            }
        });

        // find the add and remove buttons
        addButton = findViewById(R.id.increase_button);
        removeButton = findViewById(R.id.decrease_button);

        // set the on click listener for the add button to add a day to the days layout
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDay();
            }
        });

        // set the on click listener for the remove button to remove a day from the days layout
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDay();
            }
        });

    }

    public void setDateToMedicineSchedule(String date) {
        if (isSettingStartDate) {
            startDate.setText(date);
        } else {
            endDate.setText(date);
        }
    }

    public void setDatePickerFragment() {
        datePickerFragment = new DatePickerFragment();
        datePickerFragment.setAddNewMedicineActivityObj(this);
        datePickerFragment.show(getSupportFragmentManager(), "Date picker");
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void OnDateSet(DatePicker dataPicker, int year, int month, int day) {
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.YEAR, year);
        calendarStart.set(Calendar.MONTH, month);
        calendarStart.set(Calendar.DAY_OF_MONTH, day);
        String startDateFormat = DateFormat.getDateInstance().format(calendarStart.getTime());
        startDate.setText(startDateFormat);
    }

    private void enableDays(boolean isEnabled){
//        for (Button weekDayButton : weekDayButtons) {
//            weekDayButton.setEnabled(isEnabled);
//            if (isEnabled) {
//                weekDayButton.setTextColor(getResources().getColor(R.color.black));
//            }
//        }
        daysRelativeLayout = findViewById(R.id.days_relativeLayout);

        if (isEnabled){
            daysRelativeLayout.setVisibility(View.VISIBLE);
        }
        else{
            daysRelativeLayout.setVisibility(View.GONE);
        }
    }

    private void enableOneTime(boolean isOneTime){

        durationLayout = findViewById(R.id.duration_section);
        scheduleLayout = findViewById(R.id.schedule_section);

        if (isOneTime){
            durationLayout.setVisibility(View.GONE);
            scheduleLayout.setVisibility(View.GONE);
        }
        else{
            durationLayout.setVisibility(View.VISIBLE);
            scheduleLayout.setVisibility(View.VISIBLE);
        }
    }

    private void addDay(){
        if (countDays < 7){
            countDays++;
            daysText = findViewById(R.id.dayTimes_text);
            // convert the countDays to a string
            String countDaysStr = String.valueOf(countDays);
            daysText.setText(countDaysStr);
            // set time to times when day is greater than 1
            if (countDays > 1){
                timesText = findViewById(R.id.times_text);
                timesText.setText(" times");
            }
            else {
                timesText = findViewById(R.id.times_text);
                timesText.setText(" time");
            }
        }
    }

    private void removeDay(){
        if (countDays > 1){
            countDays--;
            daysText = findViewById(R.id.dayTimes_text);
            // convert the countDays to a string
            String countDaysStr = String.valueOf(countDays);
            daysText.setText(countDaysStr);
            // set time to times when day is greater than 1
            if (countDays > 1){
                timesText = findViewById(R.id.times_text);
                timesText.setText(" times");
            }
            else {
                timesText = findViewById(R.id.times_text);
                timesText.setText(" time");
            }
        }
    }
}