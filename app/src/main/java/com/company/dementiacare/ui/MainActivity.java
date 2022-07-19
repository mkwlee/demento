/*
*   Main Activity Class
*
*   Des: Every general activity such as navigation, ... will be called here!
*   Updated: July 04, 2022
*
* */

package com.company.dementiacare.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;

import com.company.dementiacare.R;
import com.company.dementiacare.databinding.ActivityMainBinding;
import com.company.dementiacare.ui.add.AddActivity;
import com.company.dementiacare.ui.home.HomeFragment;
import com.company.dementiacare.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Animation slideInAnim;
    View addFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Start app with Home Screen
        displayFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            // Switch between screens base on the id that is being passed
            switch (item.getItemId()){
                case R.id.home:
                    displayFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    displayFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // Disable the add button from menu
        bottomNavigationView.getMenu().getItem(1).setEnabled(false);

        // Add button
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigate to add activity screen
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
    }

    // Display Screens (Fragments)
    private void displayFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}