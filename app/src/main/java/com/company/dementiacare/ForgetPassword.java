/*
 * 
 *  Forgot Password Activity
 * 
 *  Description:
 *  This activity is used to reset the password of the user.
 * 
 *  Updated:
 *  July 07, 2022
 * 
 * 
*/

package com.company.dementiacare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    private Button nextBtn;
    private TextInputLayout emailTextField;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks
        emailTextField = findViewById(R.id.forget_password_email);
        nextBtn = findViewById(R.id.forget_password_next_btn);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

    }

    // This method is used to check if the email is valid or not.
    private Boolean validateEmail(){
        String val = emailTextField.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            emailTextField.setError("Field cannot be empty");
            return false;
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

    //This method will be called when the use should verify their email address
    public void verifyEmail(View view){

        if (!validateEmail()){
            return;
        }

        final String email = emailTextField.getEditText().getText().toString().trim();

        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("email")
                .equalTo(email);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    emailTextField.setError(null);
                    emailTextField.setErrorEnabled(false);

                    Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                    intent.putExtra("email", email);
                    intent.putExtra("whatToDo", "updateData");
                    startActivity(intent);
                    finish();

                }
                else{
                    emailTextField.setError("No such user exists!");
                    emailTextField.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ForgetPassword.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}