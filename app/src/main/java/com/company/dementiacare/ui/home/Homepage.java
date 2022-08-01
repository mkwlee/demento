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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.dementiacare.applayer.MedicineBusinessLayer;
import com.company.dementiacare.database.Med;
import com.company.dementiacare.ui.auth.Login;
import com.company.dementiacare.R;
import com.company.dementiacare.StaticRVAdapter;
import com.company.dementiacare.StaticRVModel;
import com.company.dementiacare.ui.profile.UserProfile;
import com.company.dementiacare.ui.add.AddActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
    MaterialButton addButton;
    TextView userName;

    // medication list
    private LinearLayout medicineLayout;
    private Context mContext;


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
//        viewAll = findViewById(R.id.viewAll);
        contentView = findViewById(R.id.content_homepage);
        helloText = findViewById(R.id.helloText);
        medicineLayout = findViewById(R.id.medicationList);

        mContext = getApplicationContext();

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

        userName = findViewById(R.id.user_name);
        userName.setText(getIntent().getStringExtra("username"));

        // greeting text
        greetUser();

        try{
            populateMedication();
        } catch (Exception e){
            e.printStackTrace();
        }

//        // set the item for the recycler view
//        setItemInfo();
//        // set the adapter for the recycler view
//        setAdapter();

        // navigation drawer
        navigationDrawer();
//        viewReminder();
    }

    private void populateMedication() throws ParseException {
        // get the upcoming medicine details
        MedicineBusinessLayer MedicineObj = new MedicineBusinessLayer();

        List<Med> MedicineList = MedicineObj.getAllMedicine(mContext);
        System.out.println("medicine list size is: "+ MedicineList.size());

        // sort medicine alphabetically
        Collections.sort(MedicineList, new Comparator<Med>() {
            @Override
            public int compare(Med medicine1, Med medicine2) {
                return medicine1.medName.compareTo(medicine2.medName);
            }
        });

        if (MedicineList != null) {
            if (MedicineList.size() != 0) {
                showMedicineOnScreen(MedicineList, medicineLayout);
            } else {
                showNoMedicationAvailable(medicineLayout, "NO MEDICATIONS ADDED");
            }
        } else {
            showNoMedicationAvailable(medicineLayout, "NO MEDICATIONS ADDED");
        }
    }

    private void showMedicineOnScreen(List<Med> MedicineArrayList, LinearLayout linearLayout) {
        for (int i = 0; i < MedicineArrayList.size(); i++) {

            CardView CardViewObj = new CardView(mContext);

            // set the layout params
            LinearLayout.LayoutParams ParamsObj = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDPI(100));

            // set the weight
            ParamsObj.weight = 1.0f;

            // set the parameters
            CardViewObj.setLayoutParams(ParamsObj);

            CardViewObj.setBackgroundColor(getResources().getColor(R.color.white));

            // add margins
            ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) CardViewObj.getLayoutParams();
            cardViewMarginParams.setMargins(getDPI(5), getDPI(5), getDPI(5), getDPI(5));

            // create a linear layout
            LinearLayout Parent = new LinearLayout(mContext);

            // set the linear layout params
            ParamsObj = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            // set the weight
            ParamsObj.weight = 1.0f;

            // set the orientation
            Parent.setOrientation(LinearLayout.HORIZONTAL);

            // set the padding
            Parent.setPadding(getDPI(5), getDPI(5), getDPI(5), getDPI(5));

            // set the layout
            Parent.setLayoutParams(ParamsObj);

            // Create the imageview object
            ImageView ChildImageObj = new ImageView(mContext);

            // set the linear layout params
            ParamsObj = new LinearLayout.LayoutParams(getDPI(180), ViewGroup.LayoutParams.WRAP_CONTENT);

            // set the weight for the image
            ParamsObj.weight = 1.0f;

            ChildImageObj.setLayoutParams(ParamsObj);

            // set the padding
            ChildImageObj.setPadding(getDPI(20), getDPI(20), getDPI(10), getDPI(20));

            // set the image ID
            int ImageID = R.drawable.outline_medication_black_24dp;

            // set the imageID
            ChildImageObj.setImageResource(ImageID);

            // add the image to the linear layout
            Parent.addView(ChildImageObj);

            LinearLayout ChildLinearLayout = new LinearLayout(mContext);

            // set the linear layout params
            ParamsObj = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            // set the weight
            ParamsObj.weight = 1.0f;

            // set the orientation
            ChildLinearLayout.setOrientation(ChildLinearLayout.VERTICAL);

            // add the params
            ChildLinearLayout.setLayoutParams(ParamsObj);


            // set the linear layout params
            ParamsObj = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            // set the weight
            ParamsObj.weight = 1.0f;

            // Start of ChildTextView1
            // create a child textview object
            TextView ChildTextView1 = new TextView(mContext);

            // set the padding
            ChildTextView1.setPadding(getDPI(5), getDPI(5), getDPI(0), getDPI(0));

            // set the text
            ChildTextView1.setText(MedicineArrayList.get(i).medName);

            // set the background color
            ChildTextView1.setBackgroundColor(getResources().getColor(R.color.white));

            // set the text size
            ChildTextView1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

            ChildTextView1.setTextColor(getResources().getColor(R.color.black));

            // now since we have all the textview parameters
            // now add the values to the linearLayout
            ChildLinearLayout.addView(ChildTextView1);
            // End of ChildTextView1

            // Start of ChildTextView2
            // create a child textview object
            TextView ChildTextView2 = new TextView(mContext);

            // set the padding
            ChildTextView2.setPadding(getDPI(5), getDPI(5), getDPI(0), getDPI(0));

            // set the text
            ChildTextView2.setText(MedicineArrayList.get(i).dosage);

            // set the text size
            ChildTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

            ChildTextView2.setTextColor(getResources().getColor(R.color.light_gray));

            // now since we have all the textview parameters
            // now add the values to the linearLayout
            ChildLinearLayout.addView(ChildTextView2);
            // End of ChildTextView1

            // Start of ChildTextView3
            // create a child textview object
            TextView ChildTextView3 = new TextView(mContext);

            // set the padding
            ChildTextView3.setPadding(getDPI(5), getDPI(5), getDPI(0), getDPI(0));

            // set the text
            ChildTextView3.setText(MedicineArrayList.get(i).startDate.toString());

            // set the text size
            ChildTextView3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

            ChildTextView3.setTextColor(getResources().getColor(R.color.light_gray));

            // now since we have all the textview parameters
            // now add the values to the linearLayout
            ChildLinearLayout.addView(ChildTextView3);
            // End of ChildTextView1


            // add to the parent
            Parent.addView(ChildLinearLayout);

            // add the linear layout to the cardview
            CardViewObj.addView(Parent);

            CardViewObj.requestLayout();

            linearLayout.addView(CardViewObj);
        }

    }

    private void showNoMedicationAvailable(LinearLayout linearLayout, String TextID){
        TextView ChildTextView2 = new TextView(mContext);

        // set the padding
        ChildTextView2.setPadding(getDPI(5), getDPI(5), getDPI(0), getDPI(0));

        // set the text
        ChildTextView2.setText(TextID);

        // set the text size
        ChildTextView2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);

        ChildTextView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ChildTextView2.setTextColor(getResources().getColor(R.color.light_gray));

        // now since we have all the textview parameters
        // now add the values to the linearLayout
        linearLayout.addView(ChildTextView2);
    }

    public int getDPI(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    // greeting user based on the time
    private void greetUser() {
        // get the users current time
        Calendar calendar = Calendar.getInstance();
        // get the hour of the day
        int currentTime = calendar.get(Calendar.HOUR_OF_DAY);
        // if the time is between 6am and 12pm, say good morning
        if (currentTime < 12) {
            helloText.setText("Good morning, ");
            // if the time is between 12pm and 6pm, say good afternoon
        } else if (currentTime >= 12 && currentTime < 18) {
            helloText.setText("Good afternoon, ");
        } else {
            // if the time is between 6pm and 12am, say good evening
            helloText.setText("Good evening, ");
        }
    }

    // Function show all or less the user's schedule
//    private void viewReminder() {
//        viewAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!checkViewAll) {
//                    // if the user clicks on view all, show all the reminders
//                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
//                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    recyclerView.setLayoutParams(params);
//                    viewAll.setText("View Less");
//                    checkViewAll = true;
//                }
//                else{
//                    // if the user clicks on view less, show only the first 3 reminders
//                    ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
//                    params.height = 800;
//                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    recyclerView.setLayoutParams(params);
//                    viewAll.setText("View All");
//                    checkViewAll = false;
//                }
//            }
//        });
//    }

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

//    // Set Schedule medicines from User's Database into an array item (For now we just testing the
//    // by the random Model item will be done fully in Sprint 3
//    private void setItemInfo(){
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 1"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 2"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 3"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 4"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 5"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 6"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 7"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 8"));
//        item.add(new StaticRVModel(R.drawable.drug_small_icon, "Reminder 9"));
//    }
//
//    // Set all Info from User's Database into Adapter
//    private void setAdapter(){
//        // set the adapter for the recycler view
//        staticRVAdapter = new StaticRVAdapter(item);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView.setAdapter(staticRVAdapter);
//    }

}