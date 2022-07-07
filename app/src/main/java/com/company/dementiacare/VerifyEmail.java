package com.company.dementiacare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {

    String name, username, email, phone ,password;

    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseAuth = FirebaseAuth.getInstance();

        //Hooks
        name  = getIntent().getStringExtra("name");
        username  = getIntent().getStringExtra("username");
        email  = getIntent().getStringExtra("email");
        phone  = getIntent().getStringExtra("phone");
        password  = getIntent().getStringExtra("password");

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        final Handler handler = new Handler();
        final int delay = 5000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (user.isEmailVerified()){
                                Intent intent = new Intent(getApplicationContext(), UserProfile.class);

                                intent.putExtra("name", name);
                                intent.putExtra("username", username);
                                intent.putExtra("phone", phone);
                                intent.putExtra("email", email);
                                intent.putExtra("password", password);

                                startActivity(intent);
                            }
                        }
                        else{
                            Toast.makeText(VerifyEmail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(this, delay);
            }
        },delay);
    }
}