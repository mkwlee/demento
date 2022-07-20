package com.company.dementiacare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UserProfile extends AppCompatActivity {

    TextView picName;
    TextInputLayout profileName, profilePhone, profileAddress, profileEmail, regPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Hooks
        picName = findViewById(R.id.full_name);
        profileName = findViewById(R.id.txt_full_name);
        profilePhone = findViewById(R.id.txt_phone);
        profileEmail = findViewById(R.id.txt_email);
        profileAddress = findViewById(R.id.txt_address);
        update_btn = findViewById(R.id.update_profile);

        //Read the data
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child("deedee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserHelper userHelper = dataSnapshot.getValue(UserHelper.class);
                picName.setText(userHelper.name);
                profileName.getEditText().setText(userHelper.name);
                profilePhone.getEditText().setText(userHelper.phone);
                profileEmail.getEditText().setText(userHelper.email);
                profileAddress.getEditText().setText(userHelper.address);
                System.out.println(userHelper);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get all the value
                String name = profileName.getEditText().getText().toString();
                String email = profileEmail.getEditText().getText().toString();
                String phone = profilePhone.getEditText().getText().toString();
                String address = profileAddress.getEditText().getText().toString();

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");
                String key = reference.push().getKey();

                UserHelper userHelper = new UserHelper("deedee", name , email, phone, address);
                Map<String, Object> postValues = userHelper.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);
                reference.updateChildren(childUpdates);

            }
        });

    }


}