package com.company.dementiacare.ui;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.widget.ScrollView;

import com.company.dementiacare.R;
import com.company.dementiacare.StaticRVAdapter;
import com.company.dementiacare.StaticRVModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    ScrollView scrollView;
    ArrayList<StaticRVModel> dailyItems = new ArrayList<>();
    ArrayList<StaticRVModel> nonDailyItems = new ArrayList<>();
    private RecyclerView dailyRecyclerView;
    private RecyclerView nonDailyRecyclerView;
    private StaticRVAdapter staticRVAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        scrollView = findViewById(R.id.calendarScrollView);
        dailyRecyclerView = findViewById(R.id.daily_calendar);
        nonDailyRecyclerView = findViewById(R.id.non_daily_calendar);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        String username = getIntent().getStringExtra("username");

        Query checkUser = reference.orderByChild("username").equalTo(username);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    StaticRVAdapter medFromDB = snapshot.child(username).child("medicines").getValue(StaticRVAdapter.class);
                    
                    // Get the daily items
                    for(int i = 0; i < medFromDB.getItems().size(); i++){
                        if(medFromDB.getItems().get(i).getTagDaily() == 1){
                            dailyItems.add(medFromDB.getItems().get(i));
                        }
                    }

                    // Get the non-daily items
                    for(int i = 0; i < medFromDB.getItems().size(); i++){
                        if(medFromDB.getItems().get(i).getTagDaily() == 0){
                            nonDailyItems.add(medFromDB.getItems().get(i));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setDailyAdapter();
    }

    private void setDailyAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(dailyItems);
        dailyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dailyRecyclerView.setAdapter(staticRVAdapter);
    }

    private void setNonDailyAdapter(){
        // set the adapter for the recycler view
        staticRVAdapter = new StaticRVAdapter(nonDailyItems);
        nonDailyRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        nonDailyRecyclerView.setAdapter(staticRVAdapter);
    }
}