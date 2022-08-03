package com.company.dementiacare.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.company.dementiacare.R;
import com.company.dementiacare.StaticRVAdapter;
import com.company.dementiacare.StaticRVModel;
import com.company.dementiacare.ui.add.AddClient;
import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.ui.home.Homepage;
import com.company.dementiacare.ui.profile.ClientProfile;
import com.company.dementiacare.ui.profile.UserProfile;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ScrollView scrollView;
    ArrayList<StaticRVModel> dailyItems = new ArrayList<>();
    ArrayList<StaticRVModel> nonDailyItems = new ArrayList<>();
    TextView messageView;

    static final float END_SCALE = 0.7f;

    private RecyclerView dailyRecyclerView;
    private RecyclerView nonDailyRecyclerView;
    private StaticRVAdapter staticRVAdapter;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    LinearLayout contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        scrollView = findViewById(R.id.calendarScrollView);
        dailyRecyclerView = findViewById(R.id.daily_calendar);
        nonDailyRecyclerView = findViewById(R.id.non_daily_calendar);
        drawerLayout = findViewById(R.id.drawer_layout_calendar);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_calendar);
        messageView = findViewById(R.id.calendar_message);
        messageView.setVisibility(View.GONE);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        String username = getIntent().getStringExtra("username");

        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    StaticRVAdapter medFromDB = snapshot.child(username).child("medicines").getValue(StaticRVAdapter.class);
                    
                    // Get the daily items
                    for(int i = 1; i < medFromDB.getItems().size(); i++){
                        if(medFromDB.getItems().get(i).getTagDaily() == 1){
                            dailyItems.add(medFromDB.getItems().get(i));
                            System.out.println("daily items: " + dailyItems.size());
                        }
                    }

                    // Get the non-daily items
                    for(int i = 1; i < medFromDB.getItems().size(); i++){
                        if(medFromDB.getItems().get(i).getTagDaily() == 0){
                            medFromDB.getItems().get(i).setDateVisible(true);
                            nonDailyItems.add(medFromDB.getItems().get(i));
                            System.out.println("nondaily items: " + nonDailyItems.size());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (dailyItems == null && nonDailyItems == null){
            messageView.setVisibility(View.VISIBLE);
        }
        else {
            messageView.setVisibility(View.GONE);
            setDailyAdapter();
            setNonDailyAdapter();
        }
        navigationDrawer();
    }

    private void navigationDrawer() {
        //Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_calendar);

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
            case R.id.nav_add_client_profile:
                // navigate to the profile page
                Intent intent3 = new Intent(getApplicationContext(), AddClient.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                finish();
                break;
            case R.id.nav_client_profile:
                // navigate to the profile page
                Intent intent4 = new Intent(getApplicationContext(), ClientProfile.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                finish();
                break;

        }
        return true;
    }

    private void setDailyAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(dailyItems);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(CalendarActivity.this, LinearLayoutManager.VERTICAL, false));
        dailyRecyclerView.setAdapter(staticRVAdapter);
    }

    private void setNonDailyAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(nonDailyItems);
        nonDailyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        nonDailyRecyclerView.setAdapter(staticRVAdapter);
    }
}