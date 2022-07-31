package com.company.dementiacare.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.company.dementiacare.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClientProfile extends AppCompatActivity {

    TextInputLayout name, age, gender, height, weight, stage;

    final String username = getIntent().getStringExtra("username");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = findViewById(R.id.client_name);
        age = findViewById(R.id.client_age);
        gender = findViewById(R.id.client_gender);
        height = findViewById(R.id.client_height);
        weight = findViewById(R.id.client_weight);
        stage = findViewById(R.id.client_stage);

        Query checkUser = reference.orderByChild("username").equalTo(username);

        //Take all data from the username's database
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // if the username is not found in the database
                if(snapshot.exists()){

                    //Take all data from the username's database
                    String passwordFromDB =
                            snapshot.child(username).child("password").getValue(String.class);

                    String emailFromDB =
                            snapshot.child(username).child("email").getValue(String.class);

                    String phoneFromDB =
                            snapshot.child(username).child("phone").getValue(String.class);

                    String nameFromDB =
                            snapshot.child(username).child("name").getValue(String.class);

                    String usernameFromDB =
                            snapshot.child(username).child("username").getValue(String.class);

                    //Show all data
                    //showAllClientData(usernameFromDB, nameFromDB, emailFromDB, phoneFromDB, passwordFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void showAllClientData() {
    }
}