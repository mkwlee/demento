/*
 *          Success Forget Password
 *
 *  Description: An activity to show that user's successful creating
 *
 *
 * updated: August 3rd 2022
 */
package com.company.dementiacare.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.company.dementiacare.R;

// The activity when you success to change the password
public class SuccessForgetPasswordMessage extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_forget_password_message);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loginBtn = findViewById(R.id.loginBtn);
        // navigate to login page
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}