
/*
 *      Sign Up Activity
 * 
 *  Description: This activity is used to register the user.
 * 
 *  updated: July 21, 2022
*/
package com.company.dementiacare.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.company.dementiacare.ClientHelper;
import com.company.dementiacare.R;
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.ui.home.Homepage;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    //Variables
    TextInputLayout regName, regUsername, regEmail, regPhone, regPassword;
    Button regBtn, regToLoginBtn;

    // Firebase variables
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks and variables
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhone = findViewById(R.id.reg_phone);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);

        // Firebase variables
        // get the reference of Firebase Database
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        // Go back to login view when the user realize they already have account
        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Some validation information's of user's name
    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        // Check if the value is null or empty
        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }else{
            // Clear the error message
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    // Some validation information's of user's username
    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";


        // Check if the value is null or empty
        if (val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
            // Check if the value is more than 15 characters
        }else if(val.length() >= 15){
            regUsername.setError("Username too long");
            return false;
            // Check if the value matches the pattern
        }else if(!val.matches(noWhiteSpace)){
            regUsername.setError("White Spaces are not allowed");
            return false;
        }
        else{
            // Clear the error message
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    // Some validation information's of user's email
    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Check if the value is null or empty
        if (val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
            // Check if the value is valid email
        }else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }
    }

    // Some validation information's of user's phone number
    private Boolean validatePhone(){
        String val = regPhone.getEditText().getText().toString();

        // Check if the value is null or empty
        if (val.isEmpty()){
            regPhone.setError("Field cannot be empty");
            return false;
        }else{
            // Clear the error message
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    // Some validation information's of user's password
    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();

        // Check if the value is null or empty
        if (val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
            // Check if the value is less than 6 characters
        }else if(val.length() <= 6){
            regPassword.setError("Password is too weak");
            return false;
        }
        else{
            // Clear the error message
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    //Save data in firebase on button click
    public void registerUser(View view){

        String username = regUsername.getEditText().getText().toString().trim();

        reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    regUsername.setError("Username is already existed");
                    regUsername.requestFocus();
                }
                else{
                    regUsername.setError(null);
                    regUsername.setErrorEnabled(false);
                    //Check all the information are validated
                    if (!validateName() | !validateUsername() | !validatePhone()
                            | !validatePassword() | !validateEmail()){
                        return;
                    }

                    //Get all the value from the text field from the user
                    String name = regName.getEditText().getText().toString().trim();
                    String email = regEmail.getEditText().getText().toString().trim();
                    String phone = regPhone.getEditText().getText().toString().trim();
                    String password = regPassword.getEditText().getText().toString().trim();

                    ClientHelper client = new ClientHelper();

                    //Storing Data in firebase
                    UserHelper helper = new UserHelper(name ,username, email, phone, password, client);
                    reference.child(username).setValue(helper);

                    // navigate to success page
                    Intent intent = new Intent(getApplicationContext(), SuccessSignUp.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}