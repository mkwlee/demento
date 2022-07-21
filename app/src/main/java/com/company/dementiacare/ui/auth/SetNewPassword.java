/*
 *          Set new password
 *
 *  Description: This activity is used to set a new password for the user.
 * 
 * updated: July 21, 2022
*/

package com.company.dementiacare.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.company.dementiacare.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    // variables
    TextInputLayout newPassword, confirmNewPassword;

    Button newPasswordBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks and variables
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        newPasswordBtn = findViewById(R.id.newPasswordButton);

    }

    // Some Validation for setting new password
    private Boolean validatePassword(){
        String val = newPassword.getEditText().getText().toString();

        // Check if the password is empty
        if (val.isEmpty()){
            newPassword.setError("Field cannot be empty");
            return false;
            // Check if the password is less than 6 characters
        }else if(val.length() <= 6){
            newPassword.setError("Password is too weak");
            return false;
        }
        else{
            // Check if the password is equal to the confirm password then return true and set the error to null
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    // Some Validation for the confirm password
    private Boolean validateConfirmPassword(){
        String val = confirmNewPassword.getEditText().getText().toString().trim();
        String val2 = newPassword.getEditText().getText().toString().trim();

        // Check if the password is empty
        if (val.isEmpty()){
            confirmNewPassword.setError("Field cannot be empty");
            return false;
            // Check if the password is equal to the new password then return true and set the error to null
        }else if(!val.equals(val2)){
            confirmNewPassword.setError("Your password does not match with your new password");
            return false;
        }
        else{
            confirmNewPassword.setError(null);
            confirmNewPassword.setErrorEnabled(false);
            return true;
        }
    }

    // When the user clicks on the button, the validation will be checked and if it is true, the new password will be set in the database
    public void setNewPasswordBtn(View view){

        //Check the validation are correct if not send the errors
        if (!validatePassword() | !validateConfirmPassword()){
            return;
        }

        // Get the user's username from the previous activity
        String _newPassword = newPassword.getEditText().getText().toString();
        String username = getIntent().getStringExtra("username");

        //Update password to firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(username).child("password").setValue(_newPassword);

        // Go to the login page
        Intent intent = new Intent(getApplicationContext(), SuccessForgetPasswordMessage.class);
        startActivity(intent);
        finish();
    }
}