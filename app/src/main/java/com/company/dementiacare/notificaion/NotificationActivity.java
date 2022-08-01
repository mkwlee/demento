package com.company.dementiacare.notificaion;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.TextView;

import com.company.dementiacare.R;
public class NotificationActivity extends AppCompatActivity {

    /*
     * Textview for the Title Page, MedicineName, MedicineDetails
     * */
    TextView title;
    TextView medName, medDosage, medColor, medType, medDes, patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_notification);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
         * Fetching details for medicine details from the notification
         * */
        Bundle extras = getIntent().getExtras();
        title=findViewById(R.id.notification_day);
        medName=findViewById(R.id.notification_name);
        medDosage=findViewById(R.id.notification_dosage);
        medType = findViewById(R.id.notification_type);
        medColor = findViewById(R.id.notification_color);
        /*
         * Displaying the details
         * */

        title.setText("Medicine Details");
        medName.setText(extras.getString("name"));
        medDosage.setText(extras.getString("description"));
        medType.setText(extras.getString("type"));
        medColor.setText(extras.getString("color"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}