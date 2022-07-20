package com.company.dementiacare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    // Tag label
    private static final String TAG = "EmailPassword";

    //Variables
    TextInputLayout regName, regUsername, regEmail, regPhone, regPassword;
    Button regBtn, regToLoginBtn;

    private FirebaseAuth mAuth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Initialize Firebase Auth
        //Hooks
        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPhone = findViewById(R.id.reg_phone);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        regToLoginBtn = findViewById(R.id.reg_login_btn);


        // go back to the login page
        regToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUp.this).toBundle());
            }
        });

        // once the register button is clicked
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(SignUp.this, UserProfile.class);
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUp.this).toBundle());


                // register the account into Fireauth
                String email = regEmail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                createAccount(email, password);

                // register the user
                registerUser(v);

                // register info to Firebase database Successful
                Toast.makeText(SignUp.this, "Sign Up Successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignUp.this, "create account successfully.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignUp.this, UserProfile.class);
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SignUp.this).toBundle());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private Boolean validateName(){
        String val = regName.getEditText().getText().toString();

        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        }else if(val.length() >= 15){
            regUsername.setError("Username too long");
            return false;
        }else if(!val.matches(noWhiteSpace)){
            regUsername.setError("White Spaces are not allowed");
            return false;
        }
        else{
            regUsername.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateEmail(){
        String val = regEmail.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(emailPattern)){
            regEmail.setError("Invalid email address");
            return false;
        }
        else{
            regEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePhone(){
        String val = regPhone.getEditText().getText().toString();

        if (val.isEmpty()){
            regPhone.setError("Field cannot be empty");
            return false;
        }else{
            regPhone.setError(null);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();

        if (val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }else if(val.length() <= 6){
            regPassword.setError("Password is too weak");
            return false;
        }
        else{
            regPassword.setError(null);
            return true;
        }
    }

    //Save data in firebase on button click
    public void registerUser(View view){

//        if (!validateName() | !validateUsername() | !validatePhone()
//                | !validatePassword() | !validateEmail()){
//            return;
//        }

        //Get all the value
        String name = regName.getEditText().getText().toString();
        String username = regUsername.getEditText().getText().toString();
        String email = regEmail.getEditText().getText().toString();
        String phone = regPhone.getEditText().getText().toString();
        String password = "Not going to save in database";
        String address = "To be filled";

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

        UserHelper helper = new UserHelper(name ,username, email, phone, password, address);

        String uid = mAuth.getUid();
        // use email as the key
        reference.child(uid).setValue(helper);
    }



}