/*
 *          Alarm receiver
 *
 *  Description: A function to send the notification to the user when the reminder upcoming
 *
 *
 * updated: August 3rd 2022
 */
package com.company.dementiacare.notificaion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        String dosage = intent.getStringExtra("dosage").toString();
        String medname = intent.getStringExtra("medname").toString();
        String patient = intent.getStringExtra("patient").toString();
        String medColor = intent.getStringExtra("color").toString();
        String medType = intent.getStringExtra("type").toString();
        String medUnit = intent.getStringExtra("unit").toString();

//      Calling the notification method to display the notification details
        DisplayNotification displayNotification= new DisplayNotification(context);
        displayNotification.createNotification(medname, dosage,
                patient, medColor, medType, medUnit);

    }

}
