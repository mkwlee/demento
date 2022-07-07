package com.company.dementiacare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class VerifyOTP extends AppCompatActivity {

    TextView pinFromUser;
    Button verifyCodeBtn;
    String name, username, email, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pinFromUser = findViewById(R.id.forget_password_email);
        verifyCodeBtn = findViewById(R.id.forget_password_next_btn);

    }
}