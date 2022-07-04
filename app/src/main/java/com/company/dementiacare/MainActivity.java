/*
*   Main Activity Class
*
*   Des: Every general activity such as navigation, ... will be called here!
*   Updated: July 04, 2022
*
* */

package com.company.dementiacare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.company.dementiacare.databinding.ActivityMainBinding;
import com.company.dementiacare.ui.home.HomeFragment;
import com.company.dementiacare.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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
    }

    // Display Screens (Fragments)
    private void displayFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}