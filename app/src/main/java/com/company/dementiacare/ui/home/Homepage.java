/*
 *      Homepage class
 * 
 *  Description: This class is used to display the homepage of the user.
 * 
 * 
 * updated: July 21, 2022
*/


package com.company.dementiacare.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.R;
import com.company.dementiacare.StaticRVAdapter;
import com.company.dementiacare.StaticRVModel;
import com.company.dementiacare.ui.profile.UserProfile;
import com.company.dementiacare.ui.add.AddActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    //Variables
    Boolean checkViewAll = false;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    TextView helloText, viewAll;
    private RecyclerView recyclerView;
    private StaticRVAdapter staticRVAdapter;
    private ArrayList<StaticRVModel> item = new ArrayList<>();
    MaterialButton addButton;

    // End scale of drawer layout
    static final float END_SCALE = 0.7f;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
//        ActionBar actionBar = getSupportActionBar();
//
//
//        actionBar.hide();
        //This line will hide the status bar from the screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout_homepage);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        viewAll = findViewById(R.id.viewAll);
        contentView = findViewById(R.id.content_homepage);
        helloText = findViewById(R.id.helloText);
        recyclerView = findViewById(R.id.reminder_recycle);

        // navigate to add page
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage.this, AddActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

            }
        });

        // greeting text
        greetUser();

        // set the item for the recycler view
        setItemInfo();
        // set the adapter for the recycler view
        setAdapter();

        // navigation drawer
        navigationDrawer();
        viewReminder();
    }

    // greeting user based on the time
    private void greetUser() {
        String name = getIntent().getStringExtra("name");
        // get the users current time
        Calendar calendar = Calendar.getInstance();
        // get the hour of the day
        int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
        // if the time is between 6am and 12pm, say good morning
        if (currentTime < 12) {
            helloText.setText("Good morning, " + name + "!");
            // if the time is between 12pm and 6pm, say good afternoon
        } else if (currentTime >= 12 && currentTime < 18) {
            helloText.setText("Good afternoon, " + name + "!");
        } else {
            // if the time is between 6pm and 12am, say good evening
            helloText.setText("Good evening, " + name + "!");
        }
    }

    // Function show all or less the user's schedule
    private void viewReminder() {
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkViewAll) {
                    // if the user clicks on view all, show all the reminders
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = 150;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    recyclerView.setLayoutParams(params);
                    viewAll.setText("View Less");
                    checkViewAll = true;
                }
                else{
                    // if the user clicks on view less, show only the first 3 reminders
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = 800;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    recyclerView.setLayoutParams(params);
                    viewAll.setText("View All");
                    checkViewAll = false;
                }
            }
        });
    }

    // Set the vertical navigation in the homepage
    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // Menu show/hide
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

        // animate the drawer layout
        animateNavigationDrawer();
    }

    // Animation when you click to the vertical navigation view
    private void animateNavigationDrawer() {

        // get the action bar and set the icon to be a hamburger
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));

        // add a listener to the drawer layout
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

    // set back pressed to the homepage instead of log out to the application
    @Override
    public void onBackPressed() {

        // if the drawer is open, close it
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    // Set new activity based on the page selected in the navigation
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            case R.id.nav_profile:
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                String username = getIntent().getStringExtra("username");
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    // Set Schedule medicines from User's Database into an array item (For now we just testing the
    // by the random Model item will be done fully in Sprint 3
    private void setItemInfo(){
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 1"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 2"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 3"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 4"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 5"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 6"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 7"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 8"));
        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 9"));
    }

    // Set all Info from User's Database into Adapter
    private void setAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRVAdapter);
    }

}