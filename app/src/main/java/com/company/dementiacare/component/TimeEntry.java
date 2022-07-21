/*
 *          TimeEntry class component
 * 
 *  Description: This class is used to control the time entry.
 * 
 *  Update History: July 21, 2022
*/

package com.company.dementiacare.component;

public class TimeEntry implements Comparable<TimeEntry>{

    // time's hour
    private int hourOfDay;
    // time's minute
    private int minute;

    // constructor
    public TimeEntry(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    // get the time's hour
    public int getHour() {return hourOfDay;}
    // get the time's minute
    public int getMinute() {return minute;}

    // compare the time entries
    @Override
    public int compareTo(TimeEntry timeEntry) {
        if(this.hourOfDay > timeEntry.hourOfDay) {
            return 1;
        }
        if(this.hourOfDay < timeEntry.hourOfDay) {
            return -1;
        }
        if(this.minute > timeEntry.minute) {
            return 1;
        }
        if(this.minute < timeEntry.minute) {
            return -1;
        }

        return 0;
    }
}
