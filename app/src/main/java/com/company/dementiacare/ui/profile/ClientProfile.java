package com.company.dementiacare.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dementiacare.ClientHelper;
import com.company.dementiacare.R;
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.ui.auth.SuccessSignUp;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClientProfile extends AppCompatActivity {

    TextInputLayout name, age, gender, height, weight, stage;
    TextView clientTextView;

    String _NAME, _AGE, _GENDER, _HEIGHT, _WEIGHT, _USERNAME, _STAGE;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        name = findViewById(R.id.client_name);
        age = findViewById(R.id.client_age);
        gender = findViewById(R.id.client_gender);
        height = findViewById(R.id.client_height);
        weight = findViewById(R.id.client_weight);
        stage = findViewById(R.id.client_stage);
        clientTextView = findViewById(R.id.client_profile_full_name);

        String username = getIntent().getStringExtra("username");
        _USERNAME = username;

        Query checkUser = reference.orderByChild("username").equalTo(username);

        //Take all data from the username's database
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // if the username is not found in the database
                if(snapshot.exists()){

                    //Take all data from the username's database
                    String clientNameFromDB =
                            snapshot.child(username).child("client").child("name").getValue(String.class);
                    _NAME = clientNameFromDB;

                    String clientAgeFromDB =
                            snapshot.child(username).child("client").child("age").getValue(String.class);
                    _AGE = clientAgeFromDB;

                    String clientGenderFromDB =
                            snapshot.child(username).child("client").child("gender").getValue(String.class);
                    _GENDER = clientGenderFromDB;

                    String clientHeightFromDB =
                            snapshot.child(username).child("client").child("height").getValue(String.class);
                    _HEIGHT = clientHeightFromDB;

                    String clientWeightFromDB =
                            snapshot.child(username).child("client").child("weight").getValue(String.class);
                    _WEIGHT = clientWeightFromDB;

                    String clientStageFromDB =
                            snapshot.child(username).child("client").child("stage").getValue(String.class);
                    _STAGE = clientStageFromDB;

                    //Show all client data
                    showAllClientData(clientNameFromDB, clientAgeFromDB, clientGenderFromDB, clientHeightFromDB, clientWeightFromDB, clientStageFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void showAllClientData(String clientNameFromDB, String clientAgeFromDB, String clientGenderFromDB, String clientHeightFromDB, String clientWeightFromDB, String clientStageFromDB) {
        name.getEditText().setText(clientNameFromDB);
        age.getEditText().setText(clientAgeFromDB);
        gender.getEditText().setText(clientGenderFromDB);
        height.getEditText().setText(clientHeightFromDB);
        weight.getEditText().setText(clientWeightFromDB);
        stage.getEditText().setText(clientStageFromDB);
    }

    // Send the Toast message when you success updating the data
    public void updateClientData(View view){
        if (isNameChanged() | isAgeChanged() | isGenderChanged() | isHeightChanged() | isWeightChanged() | isStageChanged()){
            Toast.makeText(this, "Client Data has been updated", Toast.LENGTH_LONG).show();
        }
    }

    // some checking functions that is your data changed
    private boolean isNameChanged() {
        if (!_NAME.equals(name.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("name").setValue(name.getEditText().getText().toString());
            _NAME = name.getEditText().getText().toString();
            clientTextView.setText(_NAME);
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that name is changed
    private boolean isAgeChanged() {
        if (!_AGE.equals(age.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("age").setValue(age.getEditText().getText().toString());
            _AGE = age.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that email is changed
    private boolean isGenderChanged() {
        if (!_GENDER.equals(gender.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("gender").setValue(gender.getEditText().getText().toString());
            _GENDER = gender.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that phone is changed
    private boolean isHeightChanged() {
        if (!_HEIGHT.equals(height.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("height").setValue(height.getEditText().getText().toString());
            _HEIGHT = height.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isWeightChanged() {
        if (!_WEIGHT.equals(weight.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("weight").setValue(weight.getEditText().getText().toString());
            _WEIGHT = weight.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isStageChanged() {
        if (!_STAGE.equals(stage.getEditText().getText().toString())){

            reference.child(_USERNAME).child("client").child("stage").setValue(stage.getEditText().getText().toString());
            _STAGE = stage.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

}