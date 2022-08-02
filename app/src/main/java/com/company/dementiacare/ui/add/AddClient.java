package com.company.dementiacare.ui.add;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.company.dementiacare.ClientHelper;
import com.company.dementiacare.R;
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.ui.home.Homepage;
import com.company.dementiacare.ui.profile.ClientProfile;
import com.company.dementiacare.ui.profile.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddClient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextInputLayout name, age, gender, stage, height, weight;

    Button addBtn;

    static final float END_SCALE = 0.7f;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    ImageView menuIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hooks
        drawerLayout = findViewById(R.id.drawer_layout_add_client_profile);
        navigationView = findViewById(R.id.navigation_view);
        name = findViewById(R.id.add_client_name);
        age = findViewById(R.id.add_client_age);
        gender = findViewById(R.id.add_client_gender);
        height = findViewById(R.id.add_client_height);
        weight = findViewById(R.id.add_client_weight);
        stage = findViewById(R.id.add_client_stage);
        addBtn = findViewById(R.id.add_client_update_button);

        contentView = findViewById(R.id.content_client_profile);
        menuIcon = findViewById(R.id.menu_icon);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = getIntent().getStringExtra("username");

                Query checkUser = reference.orderByChild("username").equalTo(username);

                //Take all data from the username's database
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        // if the username is not found in the database
                        if(snapshot.exists()){
                            String client_name = name.getEditText().getText().toString();
                            String client_age = age.getEditText().getText().toString();
                            String client_gender = gender.getEditText().getText().toString();
                            String client_height = height.getEditText().getText().toString();
                            String client_weight = weight.getEditText().getText().toString();
                            String client_stage = stage.getEditText().getText().toString();

                            ClientHelper newClient = new ClientHelper(client_name,client_age,client_gender,client_height,client_weight,client_stage);

                            UserHelper user =
                                snapshot.child(username).getValue(UserHelper.class);
                            if (user.getClient() == null){
                                ArrayList<ClientHelper> client = new ArrayList<ClientHelper>();
                                client.add(newClient);
                                user.setClient(client);
                                reference.child(username).setValue(user);
                            }
                            else{
                                user.getClient().add(new ClientHelper(client_name,client_age,client_gender,client_height,client_weight,client_stage));
                                reference.child(username).setValue(user);
                            }

                            Intent intent = new Intent(getApplicationContext(), Homepage.class);
                            intent.putExtra("username",username);
                            Toast.makeText(getApplicationContext(),"Client successfully added", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        //CALL the vertical navigation
        navigationDrawer();
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_add_client_profile);

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

    // The function when you click back pressed on the navigaiton drawer it will go back to the
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
            case R.id.nav_profile:
                // navigate to the profile page
                Intent intent2 = new Intent(getApplicationContext(), UserProfile.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_client_profile:
                // navigate to the profile page
                Intent intent3 = new Intent(getApplicationContext(), ClientProfile.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                finish();
                break;

        }
        return true;
    }
}