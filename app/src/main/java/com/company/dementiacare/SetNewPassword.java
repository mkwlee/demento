package com.company.dementiacare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout newPassword, confirmNewPassword;

    Button newPasswordBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword = findViewById(R.id.confirmNewPassword);
        newPasswordBtn = findViewById(R.id.newPasswordButton);

    }

    private Boolean validatePassword(){
        String val = newPassword.getEditText().getText().toString();

        if (val.isEmpty()){
            newPassword.setError("Field cannot be empty");
            return false;
        }else if(val.length() <= 6){
            newPassword.setError("Password is too weak");
            return false;
        }
        else{
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateConfirmPassword(){
        String val = confirmNewPassword.getEditText().getText().toString().trim();
        String val2 = newPassword.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            confirmNewPassword.setError("Field cannot be empty");
            return false;
        }else if(!val.equals(val2)){
            confirmNewPassword.setError("Your confirmed password does not match with your new password");
            return false;
        }
        else{
            confirmNewPassword.setError(null);
            confirmNewPassword.setErrorEnabled(false);
            return true;
        }
    }

    public void setNewPasswordBtn(View view){
        if (!validatePassword() | !validateConfirmPassword()){
            return;
        }

        String _newPassword = newPassword.getEditText().getText().toString();
        String username = getIntent().getStringExtra("username");

        //Update password to firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child(username).child("password").setValue(_newPassword);

        Intent intent = new Intent(getApplicationContext(), SuccessForgetPasswordMessage.class);
        startActivity(intent);
        finish();
    }
}