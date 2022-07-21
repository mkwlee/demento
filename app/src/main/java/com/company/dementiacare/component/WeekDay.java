/*
 *                  WeekDay class
 *  
 *  Description: This class is used to control the week day.
 * 
 *  Update History: July 20, 2022
*/


package com.company.dementiacare.component;

import java.util.ArrayList;

public class WeekDay {
    // week day's name
    private String dayString;
    // week day's time entries
    private ArrayList<TimeEntry> timeEntriesList;

    // constructor
    public WeekDay(String dayString) {
        this.dayString = dayString;
        timeEntriesList = new ArrayList<TimeEntry>();
    }

    // get the week day's time entries
    public ArrayList<TimeEntry> getTimeEntriesList() {
        return timeEntriesList;
    }

    // get the week day's name
    public String getDayString() {
        return dayString;
    }
}
