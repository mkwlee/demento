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

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        final Handler handler = new Handler();
        final int delay = 5000;

        String username = getIntent().getStringExtra("username");

        handler.postDelayed(new Runnable() {
            private Boolean check = false;
            @Override
            public void run() {
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            if (user.isEmailVerified()){
                                if (check){
                                    check = true;
                                }else{
                                    handler.removeMessages(0);
                                }
                                Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);

                                intent.putExtra("username", username);

                                startActivity(intent);
                                finish();
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