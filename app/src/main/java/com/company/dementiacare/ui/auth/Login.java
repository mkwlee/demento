/*
 *         Login class
 * 
 *  Description: This activity is used to login the user.
 * 
 * updated: July 21, 2022
*/

package com.company.dementiacare.ui.auth;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.company.dementiacare.R;
import com.company.dementiacare.ui.home.Homepage;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    // variables
    MaterialButton callSignUp, login_btn, forget_btn;
    ImageView image;
    TextView logoText, sloganText;
    TextInputLayout username, password;

    // Firebase variables
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Hooks and variables
        callSignUp = findViewById(R.id.signup_screen);
        image = findViewById(R.id.logo_image);
        logoText = findViewById(R.id.logo_name);
        sloganText = findViewById(R.id.slogan_name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_button);
        forget_btn = findViewById(R.id.forget_password_btn);


        // Call some animation when translate from login page to sign up page
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);

                // This line will make the transition from login page to sign up page smooth
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(image, "logo_image");
                pairs[1] = new Pair<View, String>(logoText, "logo_text");
                pairs[2] = new Pair<View, String>(sloganText, "text_desc");
                pairs[3] = new Pair<View, String>(username, "username_tran");
                pairs[4] = new Pair<View, String>(password, "password_tran");
                pairs[5] = new Pair<View, String>(login_btn, "button_tran");
                pairs[6] = new Pair<View, String>(callSignUp, "login_signup_tran");

                // This line will make the transition from login page to sign up page smooth
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });


    }

    // Some validation information when sign up
    private Boolean validateUsername(){
        String val = username.getEditText().getText().toString();
        // Check if the username is empty
        if (val.isEmpty()){
            // If the username is empty, then show error message
            username.setError("Field cannot be empty");
            return false;
        } else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    // validate password
    private Boolean validatePassword(){
        String val = password.getEditText().getText().toString();

        // check if the password is empty
        if (val.isEmpty()){
            // set error message
            password.setError("Field cannot be empty");
            return false;
        } else{
            // set error message to null
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    // login button
    public void loginUser(View view){
        //Validate Login Info
        // if the username and password is not empty, then login the user
        if (!validateUsername() | !validatePassword()){
            return;
        }else{
            // if the username and password is not empty, then login the user
            isUser();
        }
    }

    // Function check that is the user existed and if it is then check password correctness
    private void isUser(){
        final String userEnteredUsername = username.getEditText().getText().toString().trim();
        final String userEnteredPassword = password.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        //Take the user from database by username's texting in the login page
        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // if the user is not existed, then show error message
                if(snapshot.exists()){
                    username.setError(null);
                    username.setErrorEnabled(false);

                    // if the user is existed, then check the password
                    String passwordFromDB =
                            snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    //Check password correctness
                    if(passwordFromDB.equals(userEnteredPassword)){

                        password.setError(null);
                        password.setErrorEnabled(false);

                        // snapshot is the user's information
                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        // if the password is correct, then save the user's information to the local storage
                        Intent intent = new Intent(getApplicationContext(), Homepage.class);

                        // put the user's information into the intent
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("password", passwordFromDB);
                        // start the homepage activity
                        startActivity(intent);
                        finish();
                    }else{
                        // if the password is incorrect, then show error message
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else{
                    // if the user is not existed, then show error message
                    username.setError("No such User exist");
                    username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    // Call forget password activity
    public void callForgetPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
    }
}