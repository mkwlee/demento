package com.company.dementiacare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

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

    static final float END_SCALE = 0.7f;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ActionBar actionBar = getSupportActionBar();


        actionBar.hide();
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

        setItemInfo();
        setAdapter();


        String name = getIntent().getStringExtra("username");
        helloText.setText("Hello " + name + "!");
        
        navigationDrawer();
        viewReminder();
    }

    // Function show all or less the user's schedule
    private void viewReminder() {
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkViewAll) {
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    recyclerView.setLayoutParams(params);
                    viewAll.setText("View Less");
                    checkViewAll = true;
                }
                else{
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

    // Animation when you click to the vertical navigation view
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

    // set back pressed to the homepage instead of log out to the application
    @Override
    public void onBackPressed() {

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
    // by the random Model item
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
        staticRVAdapter = new StaticRVAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRVAdapter);
    }

}