package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments, new TimersFragment()).commit();
    }

     BottomNavigationView.OnItemSelectedListener navListener =
        item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_timers) {
                selectedFragment = new TimersFragment();
            } else if (item.getItemId() == R.id.nav_workouts) {
                selectedFragment = new WorkoutsFragment();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new SettingsFragment();
            }

            if (selectedFragment == null) {
                Log.d("TimerValues", "Fragment is null, return");
                return false;
            }

            FragmentManager supportFragment = getSupportFragmentManager();
            FragmentTransaction transaction = supportFragment.beginTransaction();

            transaction.replace(R.id.main_fragments, selectedFragment).commit();

            return true;
        };
}
