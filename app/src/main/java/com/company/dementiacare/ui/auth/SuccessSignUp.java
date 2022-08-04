/*
 *          Success Sign Up
 *
 *  Description: An activity to show that user's successful changing password after the forget password activity
 *
 *
 * updated: August 3rd 2022
 */
package com.company.dementiacare.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.dementiacare.R;

//The activity when you success to sign up a new account
public class SuccessSignUp extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_sign_up);

        loginBtn = findViewById(R.id.success_signup_loginBtn);

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