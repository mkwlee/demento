package com.company.dementiacare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class UserProfile extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    TextView picName, picUid;
    TextInputLayout profileName, profilePhone, profileAddress, profileEmail, regPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    Button update_btn;
    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        System.out.println("mauth uid" + uid);
//        Log.d(TAG, "mauth uid"+uid );


        //Hooks
        picName = findViewById(R.id.full_name);
        picUid = findViewById(R.id.user_id);
        profileName = findViewById(R.id.txt_full_name);
        profilePhone = findViewById(R.id.txt_phone);
        profileEmail = findViewById(R.id.txt_email);
        profileAddress = findViewById(R.id.txt_address);
        update_btn = findViewById(R.id.update_profile);

        //Read the data
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        reference.child("CnUw8CvixYSgSb3PKe5YZlK1MPj2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserHelper userHelper = dataSnapshot.getValue(UserHelper.class);
                picName.setText(userHelper.name);
                picUid.setText(userHelper.username);
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
                reference = rootNode.getReference("users").child(uid);
                String key = reference.push().getKey();

                UserHelper userHelper = new UserHelper(name, email, phone, address);
                Map<String, Object> postValues = userHelper.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put(key, postValues);
                reference.updateChildren(childUpdates);

            }
        });

    }


}