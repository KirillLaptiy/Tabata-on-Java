package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.home_fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.main_activity);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.getSupportFragmentManager().popBackStack());

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragments, new HomeFragment()).commit();
    }

     BottomNavigationView.OnItemSelectedListener navListener =
        item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.home_fragment) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.food_fragment) {
                selectedFragment = new WorkoutsFragment();
            } else if (item.getItemId() == R.id.settings_fragment) {
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
