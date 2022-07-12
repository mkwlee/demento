package com.company.dementiacare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.SetSchemaRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPassword extends AppCompatActivity {

    private Button nextBtn;
    private TextInputLayout usernameTextField, emailTextField;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseAuth firebaseAuth;


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
        usernameTextField = findViewById(R.id.forget_password_username);
        emailTextField = findViewById(R.id.forget_password_email);
        nextBtn = findViewById(R.id.forget_password_next_btn);


    }

    private Boolean validateUsername(){
        String val = usernameTextField.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            usernameTextField.setError("Field cannot be empty");
            return false;
        }else if(val.length() >= 15){
            usernameTextField.setError("Username too long");
            return false;
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


    public void verifyUsername(View view){

        if (!validateUsername() | !validateEmail()){
            return;
        }

        final String username = usernameTextField.getEditText().getText().toString().trim();
        final String email = emailTextField.getEditText().getText().toString().trim();

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    usernameTextField.setError(null);
                    usernameTextField.setErrorEnabled(false);
                    emailTextField.setError(null);
                    emailTextField.setErrorEnabled(false);

                    String emailFromDB =
                            snapshot.child(username).child("email").getValue(String.class);

                    if(emailFromDB.equals(email)){

                        emailTextField.setError(null);
                        emailTextField.setErrorEnabled(false);

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