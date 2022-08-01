package com.company.dementiacare.notificaion;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.company.dementiacare.database.AppDatabase;
import com.company.dementiacare.database.Med;


public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Med med = AppDatabase.getInMemoryDatabase(context).medModel().loadMedByName(intent.getStringExtra("medName").toString());
        /*
         *Calling the notification method to display the notification details
         */

        DisplayNotification displayNotification= new DisplayNotification(context);
        displayNotification.createNotification(med.medName,"Dosage: "+(med.dosage));

    }

}
