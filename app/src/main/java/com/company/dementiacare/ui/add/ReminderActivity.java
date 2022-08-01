
/*
 *      ReminderActivity class
 *
 *  Description: This class is used to create a reminder for the user.
 *
 *  updated: July 21, 2022
 */

package com.company.dementiacare.ui.add;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.applayer.AddNewMedicineLayer;
import com.company.dementiacare.component.DatePickerFragment;
import com.company.dementiacare.component.MedicineReminder;
import com.company.dementiacare.component.TimeEntry;
import com.company.dementiacare.component.TimeListAdapter;
import com.company.dementiacare.component.TimePickerFragment;
import com.company.dementiacare.component.WeekDay;
import com.company.dementiacare.database.AppDatabase;
import com.company.dementiacare.notificaion.AlarmReceiver;
import com.company.dementiacare.ui.home.Homepage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ReminderActivity extends AppCompatActivity{

    // Variables

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    String userEmail;

    public AppDatabase appData;
    // call the medicineReminder to save reminder data
    private MedicineReminder medicineReminder;

    // text input fields
    private TextInputEditText startDate, endDate;

    TextInputLayout startDateLayout, endDateLayout;

    // call the time picker fragment
    private TimePickerFragment timePickerFragment;
    // call the date picker fragment
    private DatePickerFragment datePickerFragment;
    //a boolean to check if the day is selected or not
    boolean isSettingStartDate;

    String currentRadio;

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

    // relative layout for the days
    private RelativeLayout daysRelativeLayout;
    // linear layout for the duration, schedule and save button
    private LinearLayout durationLayout, scheduleLayout;

    MaterialButton saveButton;

    // radio group for the schedule
    private RadioGroup scheduleRadioGroup;
    private ImageButton removeButton, backButton;
    MaterialButton addButton;
    private TextView dayTimesText, timesText, daysText;

    private MaterialButton recurringButton, oneTimeButton;

    boolean oneTime = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

//        // get the users email to send email
//        final String username = getIntent().getStringExtra("username");
//
//        Query checkUser = reference.orderByChild("username").equalTo(username);
//        //Take all data from the username's database
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                // if the username is not found in the database
//                if(snapshot.exists()){
//
//                    //Take email data from the username's database
//
//                    String emailFromDB =
//                            snapshot.child(username).child("email").getValue(String.class);
//                    userEmail = emailFromDB;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });

        addButton = findViewById(R.id.add_button);
        addButton.setEnabled(false);
        // disable save button
        saveButton = findViewById(R.id.save_button);
        saveButton.setEnabled(false);
        currentRadio = "daily";

        medicineReminder = new MedicineReminder();

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
                oneTime = false;
                addButton.setEnabled(true);
                currentDay = new WeekDay("AllDays");
                enableOneTime(false);
                // change the button color
                recurringButton.setBackgroundTintList(getResources().getColorStateList(R.color.teal_200));
                oneTimeButton.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue_button));
                clearCurrentSchedule();
                // set the days layout to not visible
                enableDays(false);
                // set the current day to all days to null
                ReminderActivity.this.currentDayButton = null;
                // set the list adapter to the current day
                setListViewAdapter(currentDay);
                saveButton.setEnabled(true);
            }
        });
        // disable the schedule and the duration section when one time and change the button
        oneTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTime = true;
                currentDay = new WeekDay("AllDays");
                enableOneTime(true);
                addButton.setEnabled(true);
                // change the button color
                oneTimeButton.setBackgroundTintList(getResources().getColorStateList(R.color.teal_200));
                recurringButton.setBackgroundTintList(getResources().getColorStateList(R.color.light_blue_button));
                clearCurrentSchedule();
                // set the days layout to not visible
                enableDays(false);
                // set the current day to all days to null
                ReminderActivity.this.currentDayButton = null;
                // set the list adapter to the current day
                setListViewAdapter(currentDay);
                saveButton.setEnabled(true);
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
                            addButton.setEnabled(true);
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
                    currentRadio = "daily";
                    addButton.setEnabled(true);
                    // clear the list view
                    clearCurrentSchedule();
                    // set the days layout to not visible
                    enableDays(false);
                    // set the current day to all days to null
                    ReminderActivity.this.currentDayButton = null;
                    // set the list adapter to the current day
                    setListViewAdapter(currentDay);
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(true);
                    currentRadio = "days";
                    addButton.setEnabled(false);
                    // clear the list view
                    clearCurrentSchedule();
                    // set the days layout to visible
                    enableDays(true);
                    // set the list adapter to the current day
                    setListViewAdapter(currentDay);
                }
            }
        });
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

        // when the save button is clicked, send a toast to the user and save entered data
        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // make a toast and let them know this is for sprint 3!
                String radioOption = currentRadio;
                boolean startDateIsSet = (null != medicineReminder.getStartDate());
                boolean endDateIsSet = (null != medicineReminder.getEndDate());

                // check if no time was scheduled. In this case SAVE button will not perform saving
                boolean administrationTimeIsScheduled = false;

                if (radioOption == "daily" || oneTime == true) {
                    administrationTimeIsScheduled = !currentDay.getTimeEntriesList().isEmpty();
                } else {
                    for (WeekDay weekDay : buttonIdStrToWeekDayMap.values()) {
                        if (!weekDay.getTimeEntriesList().isEmpty()) {
                            administrationTimeIsScheduled = true;
                            break;
                        }
                    }
                }

                boolean scheduleDataIsValidForSaving =
                        startDateIsSet &&
                                endDateIsSet &&
                                administrationTimeIsScheduled;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!scheduleDataIsValidForSaving) {
                            saveButton.setEnabled(false);
                            return false;
                        }
                        return false;

                    case MotionEvent.ACTION_UP:
                        if (buttonNotInFocus(view, event)) {
                            return false;
                        }

                        if (!scheduleDataIsValidForSaving) {
                            saveButton.setEnabled(true);

                            if (!startDateIsSet) {
                                Toast.makeText(getApplicationContext(), "Please set start date", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            if (!endDateIsSet) {
                                Toast.makeText(getApplicationContext(), "Please set end date", Toast.LENGTH_SHORT).show();
                                return false;
                            }

                            if (!administrationTimeIsScheduled) {
                                Toast.makeText(getApplicationContext(), "Please schedule time for medicine", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                            return false;
                        }
                        // if we are in weekly mode, make each week day have the same, but
                        // independent time entries list
                        if (radioOption == "daily" || oneTime == true) {
                            for (WeekDay weekDay : buttonIdStrToWeekDayMap.values()) {
                                ArrayList<TimeEntry> timeEntriesList = weekDay.getTimeEntriesList();
                                for (TimeEntry timeEntry : currentDay.getTimeEntriesList())
                                    timeEntriesList.add(new TimeEntry(timeEntry.getHour(), timeEntry.getMinute()));
                            }
                        }
                        if (radioOption == "daily" || oneTime == true) {
                            medicineReminder.setIsDaily(1);
                        } else {
                            medicineReminder.setIsDaily(0);
                        }

                        //not setting start and end dates here. They are set in date picker
                        for (int weekDayIndex = 0; weekDayIndex < weekDaysArr.length; ++weekDayIndex) {
                            int weekDayId = getResources().getIdentifier(weekDaysArr[weekDayIndex], "id",
                                    getApplicationContext().getPackageName());
                            timeEntriesStrings = new ArrayList<>();

                            for (TimeEntry timeEntry : buttonIdStrToWeekDayMap.get(Integer.toString(weekDayId)).getTimeEntriesList()) {
                                String time = String.format("%02d", timeEntry.getHour()) + ":" +
                                        String.format("%02d", timeEntry.getMinute());
                                timeEntriesStrings.add(time);
                            }
                            medicineReminder.getWeekSchedule().add(timeEntriesStrings); //ArrayList<Arraylist<Strings>> builds here.
                        }
//                        String savingMessage = "Saving";
//                        SpannableString spannableString = new SpannableString(savingMessage);
//                        spannableString.setSpan(new RelativeSizeSpan(2f), 0, spannableString.length(), 0);
//                        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
//
//                        ProgressDialog progressWheelDialog = new ProgressDialog(ReminderActivity.this);
//                        progressWheelDialog.setMessage(spannableString);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(ReminderActivity.this, Homepage.class);
                                startActivity(i);
//                                overridePendingTransition(R.anim.slide_out_bottom, R.anim.slide_in_bottom);
                            }
                        }, 1500);

                        try {

                            String patient = medicineReminder.getPatient();
                            String medName = medicineReminder.getName();
                            String medType = medicineReminder.getType();
                            String medColor = medicineReminder.getColor();
                            String medDosage = medicineReminder.getDosage();
                            String medUnit = medicineReminder.getUnit();
                            String medDes = medicineReminder.getDes();
                            String medStartDate = medicineReminder.getStartDate();
                            String medEndDate = medicineReminder.getEndDate();

                            int tagDaily = 0;

                            if (Objects.equals(radioOption, "daily") || oneTime) {
                                tagDaily = 1;
                            }

                            // Add medicine data into internal database
                            AddNewMedicineLayer.AddMedicine(appData, tagDaily, medName, medDosage, medColor, medUnit, patient, medType, medDes, medStartDate, medEndDate, medicineReminder.getWeekSchedule());

                            //Parsing data into a particular format.
                            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");

                            //Calculating the delta between start/endate.
                            int daysToRun = Integer.parseInt(medEndDate.split("/")[1]) -
                                    Integer.parseInt(medStartDate.split("/")[1]);

                            /*This iteration is for adding the numerals to the date in a single month.*/
                            List<String> _lst = new ArrayList<String>();
                            int incrementDay = Integer.parseInt(medStartDate.split("/")[1]);
                            for (int i = 0; i < daysToRun; i++) {
                                if (incrementDay < 10) {
                                    /*fetching if the date is unit place or tens place*/
                                    _lst.add(medStartDate.replace(medStartDate.split("/")[1], "0" + String.valueOf(incrementDay)));
                                } else {
                                    _lst.add(medStartDate.replace(medStartDate.split("/")[1], String.valueOf(incrementDay)));
                                }
                                incrementDay++;                                /*Increementing dates*/
                            }

                            int count = 0; /*Counter for keeping the _lst values for stationary untill all the timers are alarmed and done.*/
                            /*This iteration gets the proper parsed formatted DATE. So that timely alarmed notifications can be called.*/
                            for (int k = 0; k <= 6; k++) {
                                if (medicineReminder.getWeekSchedule().get(k).size() > 0) {
                                    int git = medicineReminder.getWeekSchedule().get(k).size();
                                    for (int j = 0; j < git; j++) {
                                        Date date = format.parse(_lst.get(count) + " " + medicineReminder.getWeekSchedule().get(k).get(j));
                                        handleNotification(date.getTime(), medName);
                                    }
                                    count++;
                                }
                            }
                            //This function will send email to the user with the selected medication.
                            SendMail(medName, medDosage, medType, patient, medStartDate, medEndDate, medicineReminder.getWeekSchedule());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                }
                return false;
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
            if (null !=medicineReminder.getEndDate()){
                if(date.compareTo(medicineReminder.getEndDate()) < 0){
                    medicineReminder.setStartDate(date);
                    startDate.setText(date);
                } else{
                    Toast.makeText(getApplicationContext(), "Date can not be set - start day must occur before end date", Toast.LENGTH_LONG).show();
                }
            } else {
                medicineReminder.setStartDate(date);
                startDate.setText(date);
            }
        } else {
            if (null != medicineReminder.getStartDate()) {
                if (date.compareTo(medicineReminder.getStartDate()) > 0) {
                    medicineReminder.setEndDate(date);
                    endDate.setText(date);
                } else {
                    Toast.makeText(getApplicationContext(), "Date can not be set - end day must occur after start date", Toast.LENGTH_LONG).show();
                }
            } else {
                medicineReminder.setEndDate(date);
                endDate.setText(date);
            }
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
                weekDayButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.light_blue));
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

    //region Handling notifications using receiver broadcast mechanism.
    private void handleNotification(long intmill, String medname) {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        alarmIntent.putExtra("medName", medname);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    intmill, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, intmill, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, intmill, pendingIntent);
        }
    }

    //region SENDING EMAIL.
    public void SendMail(String medName, String dosage, String type, String patient, String startDay, String
            endDay, ArrayList<ArrayList<String>> timeObject) {
        final String username = "appdemento@gmail.com";
        final String password = "Dementoapp10.";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });

        try {
            String email_to = AddNewMedicineLayer.GetEmailId(appData);
            StringBuilder listString = new StringBuilder();
            listString.append(String.format(
                    "%s\n %s\n %s\n %s\n %s\n", medName, dosage, type, patient, startDay, endDay, timeObject));
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email_to));
            message.setSubject("Demento medicine reminder.");
            message.setText("Hi : ,"
                    + "\n\n" + listString);
            new SendMailTask().execute(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    //region ASYNC CALL TO SEND EMAILS.
    @SuppressLint("StaticFieldLeak")
    private class SendMailTask extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ReminderActivity.this, null, "Sending mail", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (SendFailedException ee) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return "error1";
            } catch (MessagingException e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                return "error2";
            }
        }
    }
    //endregion
}