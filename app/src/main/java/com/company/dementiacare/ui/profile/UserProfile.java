package com.company.dementiacare.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dementiacare.R;
import com.company.dementiacare.ui.add.AddClient;
import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.ui.home.Homepage;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables
    TextInputLayout fullname, email, phone, password;
    TextView fullNameLabel, usernameLabel;
    ImageView menuIcon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    //Global Variablies
    String _USERNAME, _NAME, _EMAIL, _PHONE, _PASSWORD;

    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final String username = getIntent().getStringExtra("username");

        //Hooks and variables
        drawerLayout = findViewById(R.id.drawer_layout_profile);
        navigationView = findViewById(R.id.navigation_view);
        fullname = findViewById(R.id.name_profile);
        email = findViewById(R.id.email_profile);
        phone = findViewById(R.id.phone_profile);
        password = findViewById(R.id.password_profile);
        fullNameLabel = findViewById(R.id.full_name_profile);
        usernameLabel = findViewById(R.id.username_profile);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_profile);

        Query checkUser = reference.orderByChild("username").equalTo(username);

        //Take all data from the username's database
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // if the username is not found in the database
                if(snapshot.exists()){

                    //Take all data from the username's database
                    String passwordFromDB =
                            snapshot.child(username).child("password").getValue(String.class);
                    _PASSWORD = passwordFromDB;

                    String emailFromDB =
                            snapshot.child(username).child("email").getValue(String.class);
                    _EMAIL = emailFromDB;

                    String phoneFromDB =
                            snapshot.child(username).child("phone").getValue(String.class);
                    _PHONE = phoneFromDB;

                    String nameFromDB =
                            snapshot.child(username).child("name").getValue(String.class);
                    _NAME = nameFromDB;

                    String usernameFromDB =
                            snapshot.child(username).child("username").getValue(String.class);
                    _USERNAME = usernameFromDB;

                    //Show all data
                    showAllUserData(usernameFromDB, nameFromDB, emailFromDB, phoneFromDB, passwordFromDB);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //CALL the vertical navigation
        navigationDrawer();
    }

    // show all the data in the text
    public void showAllUserData(String user_username, String user_name, String user_email, String user_phone, String user_password) {

        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        fullname.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phone.getEditText().setText(user_phone);
        password.getEditText().setText(user_password);
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_profile);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
    }

    //Some animation when you call or slide the vertical navigation
    private void animateNavigationDrawer() {

        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    // The function when you click back pressed on the navigation drawer it will go back to the
    // Profile instead of closing the apps
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    // set new activity based on the click pages from the navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        String username = getIntent().getStringExtra("username");
        switch (item.getItemId()){
            case R.id.nav_logout:
            // navigate to the login page
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            case R.id.nav_home:
            // navigate to the home page
                Intent intent1 = new Intent(getApplicationContext(), Homepage.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                finish();
                break;
            case R.id.nav_client_profile:
                // navigate to the client profile page
                Intent intent2 = new Intent(getApplicationContext(), ClientProfile.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_add_client_profile:
                // navigate to the client profile page
                Intent intent3 = new Intent(getApplicationContext(), AddClient.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                finish();
                break;
        }
        return true;
    }

    // Send the Toast message when you success updating the data
    public void update(View view){
        if (isNameChanged() | isPasswordChanged() | isPhoneChanged() | isEmailChanged()){
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
        }
    }

    // some checking functions that is your data changed
    private boolean isPasswordChanged() {
        if (!_PASSWORD.equals(password.getEditText().getText().toString())){

            reference.child(_USERNAME).child("password").setValue(password.getEditText().getText().toString());
            _PASSWORD = password.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that name is changed
    private boolean isNameChanged() {
        if (!_NAME.equals(fullname.getEditText().getText().toString())){

            reference.child(_USERNAME).child("name").setValue(fullname.getEditText().getText().toString());
            _NAME = fullname.getEditText().getText().toString();
            fullNameLabel.setText(fullname.getEditText().getText().toString());
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that email is changed
    private boolean isEmailChanged() {
        if (!_EMAIL.equals(email.getEditText().getText().toString())){

            reference.child(_USERNAME).child("email").setValue(email.getEditText().getText().toString());
            _EMAIL = email.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }

    // some checking functions that phone is changed
    private boolean isPhoneChanged() {
        if (!_PHONE.equals(phone.getEditText().getText().toString())){

            reference.child(_USERNAME).child("phone").setValue(phone.getEditText().getText().toString());
            _PHONE = phone.getEditText().getText().toString();
            return true;
        }
        else{
            return false;
        }
    }
}