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
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.dementiacare.ClientHelper;
import com.company.dementiacare.MainActivity;
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.ui.CalendarActivity;
import com.company.dementiacare.ui.add.AddClient;
import com.company.dementiacare.MainActivity;
import com.company.dementiacare.UserHelper;
import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.R;
import com.company.dementiacare.StaticRVAdapter;
import com.company.dementiacare.StaticRVModel;
import com.company.dementiacare.ui.auth.SuccessSignUp;
import com.company.dementiacare.ui.profile.ClientProfile;
import com.company.dementiacare.ui.profile.UserProfile;
import com.company.dementiacare.ui.add.AddActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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
    CardView card1, card2, card3;
    Animation leftAnim,rightAnim;
    GifImageView ambulance;

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
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        ambulance = findViewById(R.id.ambulance);


        //Animations for the logo and slogan
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        rightAnim = AnimationUtils.loadAnimation(this,R.anim.slide_in_right);

        card1.setAnimation(leftAnim);
        card2.setAnimation(rightAnim);
        card3.setAnimation(leftAnim);
        ambulance.setAnimation(leftAnim);

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alz.org/alzheimers-dementia/what-is-dementia"));
                startActivity(browserIntent);
            }
        });

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alzheimers.org.uk/"));
                startActivity(browserIntent);
            }
        });

        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.alzheimers.gov/life-with-dementia/tips-caregivers"));
                startActivity(browserIntent);
            }
        });


        // navigate to add page
        addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Homepage.this, AddActivity.class);
                String username = getIntent().getStringExtra("username");
                i.putExtra("username", username);
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
        viewReminder();

        // navigation drawer
        navigationDrawer();
    }

    // greeting user based on the time
    private void greetUser() {
        String name = getIntent().getStringExtra("username");
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

//    // Function show all or less the user's schedule
    private void viewReminder() {
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkViewAll) {
                    // if the user clicks on view all, show all the reminders
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT + 1500;
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    recyclerView.setLayoutParams(params);
                    viewAll.setText("View Less");
                    checkViewAll = true;
                }
                else{
                    // if the user clicks on view less, show only the first 3 reminders
                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
                    params.height = 300;
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
        String username = getIntent().getStringExtra("username");
        switch (item.getItemId()){
            case R.id.nav_logout:
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
            case R.id.nav_profile:
                Intent intent1 = new Intent(getApplicationContext(), UserProfile.class);
                intent1.putExtra("username", username);
                startActivity(intent1);
                finish();
                break;
            case R.id.nav_client_profile:
                Intent intent2 = new Intent(getApplicationContext(), ClientProfile.class);
                intent2.putExtra("username", username);
                startActivity(intent2);
                finish();
                break;
            case R.id.nav_add_client_profile:
                Intent intent3 = new Intent(getApplicationContext(), AddClient.class);
                intent3.putExtra("username", username);
                startActivity(intent3);
                finish();
                break;
            case R.id.nav_calendar:
                Intent intent4 = new Intent(getApplicationContext(), CalendarActivity.class);
                intent4.putExtra("username", username);
                startActivity(intent4);
                finish();
                break;

        }
        return true;
    }

    // Set Schedule medicines from User's Database into an array item (For now we just testing the
    // by the random Model item will be done fully in Sprint 3
    private void setItemInfo(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        String username = getIntent().getStringExtra("username");

        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    StaticRVAdapter medFromDB =
                            snapshot.child(username).child("medicines").getValue(StaticRVAdapter.class);
                    for (int i = 1; i < medFromDB.getItemCount(); i++) {
                        item.add(medFromDB.getItems().get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    // Set all Info from User's Database into Adapter
    private void setAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(staticRVAdapter);
    }

}
