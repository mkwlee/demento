/*
 *      Forgot Password Activity
 * 
 *  Description: This activity is used to reset the password of the user.
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

import com.company.dementiacare.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//The idea is the user put in the username and the email which is register in the username's data if all correct then
//Send the access to setting new password

public class ForgetPassword extends AppCompatActivity {

    // variables
    private Button nextBtn;
    private TextInputLayout usernameTextField, emailTextField;

    // Firebase variables
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks and variables
        usernameTextField = findViewById(R.id.forget_password_username);
        emailTextField = findViewById(R.id.forget_password_email);
        nextBtn = findViewById(R.id.forget_password_next_btn);
    }

    // Some validation for the info username
    private Boolean validateUsername(){
        String val = usernameTextField.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        // Check if the username is empty
        if (val.isEmpty()){
            // Set error message
            usernameTextField.setError("Field cannot be empty");
            return false;
            // Check if the username is more than 15 characters
        }else if(val.length() >= 15){
            usernameTextField.setError("Username too long");
            return false;
            // Check if the username is match with the database
        }else if(!val.matches(noWhiteSpace)){
            usernameTextField.setError("White Spaces are not allowed");
            return false;
        }
        else{
            usernameTextField.setError(null);
            usernameTextField.setErrorEnabled(false);
            return true;
        }
    }

    // Some validation for the info email
    private Boolean validateEmail(){
        String val = emailTextField.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        // Check if the email is empty
        if (val.isEmpty()){
            emailTextField.setError("Field cannot be empty");
            return false;
            // Check if the email is match with the pattern
        }else if(!val.matches(emailPattern)){
            emailTextField.setError("Invalid email address");
            return false;
        }
        else{
            emailTextField.setError(null);
            emailTextField.setErrorEnabled(false);
            return true;
        }
    }


    // verify the username and email and send the access to the next activity
    public void verifyUsername(View view){
        //Validate all the info
        if (!validateUsername() | !validateEmail()){
            return;
        }

        final String username = usernameTextField.getEditText().getText().toString().trim();
        final String email = emailTextField.getEditText().getText().toString().trim();

        // Firebase variables
        // Get the reference of the root node
        rootNode = FirebaseDatabase.getInstance();
        // Get the reference of the node "users"
        reference = rootNode.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        // Take the username's database for comparison
        Query checkUser = reference.orderByChild("username").equalTo(username);

        // Check if the username is correct and sign in the user
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //If the users exist in the database
                if(snapshot.exists()){
                    usernameTextField.setError(null);
                    usernameTextField.setErrorEnabled(false);
                    emailTextField.setError(null);
                    emailTextField.setErrorEnabled(false);

                    String emailFromDB =
                            snapshot.child(username).child("email").getValue(String.class);

                    //Check email if it's correct with the email in the user's database
                    if(emailFromDB.equals(email)){
                        emailTextField.setError(null);
                        emailTextField.setErrorEnabled(false);

                        // intent to the next activity
                        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }else{
                        emailTextField.setError("Wrong Email");
                        emailTextField.requestFocus();
                    }
                }
                else{
                    usernameTextField.setError("No such User exist");
                    usernameTextField.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}