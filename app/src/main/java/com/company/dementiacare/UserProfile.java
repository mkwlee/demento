package com.company.dementiacare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfile extends AppCompatActivity {

    String _USERNAME, _NAME, _PASSWORD, _EMAIL, _PHONE;
    TextInputLayout fullname, email, phone, password;
    TextView fullNameLabel, usernameLabel;
    Button editButton;
    private boolean editing = false;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // get the reference from firebase
        reference = FirebaseDatabase.getInstance().getReference("users");

        //Hooks
        fullname = findViewById(R.id.name_profile);
        email = findViewById(R.id.email_profile);
        phone = findViewById(R.id.phone_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.full_name_profile);
        usernameLabel = findViewById(R.id.username_profile);

        editButton = findViewById(R.id.edit_button);

        // enable editing when edit button is clicked
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editing) {
                    editing = true;
                    editButton.setText("Done");
                    fullname.setEnabled(true);
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    password.setEnabled(true);
                } else {
                    if (!validateName() | !validatePhone() | !validatePassword() | !validateMail()) {
                        return;
                    }
                    editing = false;
                    editButton.setText("Edit");
                    fullname.setEnabled(false);
                    phone.setEnabled(false);
                    email.setEnabled(false);
                    password.setEnabled(false);
//                    updateUserData();
                }
            }
        });

        //Show all data
        showAllUserData();
    }

    private void showAllUserData(){

        Intent intent = getIntent();
        _USERNAME = intent.getStringExtra("username");
        _NAME = intent.getStringExtra("name");
        _PHONE = intent.getStringExtra("phone");
        _EMAIL = intent.getStringExtra("email");
        _PASSWORD = intent.getStringExtra("password");


        fullNameLabel.setText(_NAME);
        usernameLabel.setText(_USERNAME);
        fullname.getEditText().setText(_NAME);
        email.getEditText().setText(_EMAIL);
        phone.getEditText().setText(_PHONE);
        password.getEditText().setText(_PASSWORD);
    }

    private boolean validateName () {

        String val = fullname.getEditText().getText().toString();

        if (val.isEmpty()) {
            fullname.setError("Name is required");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePhone () {

        String val = phone.getEditText().getText().toString();

        if (val.isEmpty()) {
            phone.setError("Phone is required");
            return false;
        }
        // check if the phone number is valid and if it is, show error message
        else if (!val.matches("^[0-9]{10}$")) {
            phone.setError("Phone is invalid");
            return false;
        }
        else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateMail () {

        String val = email.getEditText().getText().toString();

        if (val.isEmpty()) {
            email.setError("Mail is required");
            return false;
        } else if (!val.matches("^[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+$")) {
            email.setError("Mail is invalid");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        if (val.isEmpty()){
            password.setError("Field cannot be empty");
            return false;
        }else if(val.length() <= 6){
            password.setError("Password is too weak");
            return false;
        }
        else{
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
//
//    private void updateUserData(){
//        if(isNameChanged() || isPasswordChanged() || isEmailChanged() || isPhoneChanged()){
//            Toast.makeText(this, "Profile has been updated", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    private boolean isPhoneChanged() {
//        if(_PHONE.equals(phone.getEditText().getText().toString())){
//            reference.child(_USERNAME).child("phone").setValue(phone.getEditText().getText().toString());
//            _PHONE = phone.getEditText().getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    private boolean isEmailChanged() {
//        if(_EMAIL.equals(email.getEditText().getText().toString())){
//            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
//            _EMAIL = email.getEditText().getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    private boolean isPasswordChanged() {
//        if(_PASSWORD.equals(password.getEditText().getText().toString())){
//            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
//            _PASSWORD = password.getEditText().getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
//
//    private boolean isNameChanged() {
//
//        if(_NAME.equals(fullname.getEditText().getText().toString())){
//            reference.child(_USERNAME).child("name").setValue(fullname.getEditText().getText().toString());
//            _NAME = fullname.getEditText().getText().toString();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
}