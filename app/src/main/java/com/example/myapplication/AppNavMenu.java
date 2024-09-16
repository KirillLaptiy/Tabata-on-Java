package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppNavMenu extends AppCompatActivity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_nav_menu);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(navListener);

        // Открываем первый фрагмент по умолчанию
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Timers()).commit();
    }

    // Слушатель нажатий на элементы меню
    private final BottomNavigationView.OnItemSelectedListener navListener =
        item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_timers) {
                selectedFragment = new Timers();
            } else if (item.getItemId() == R.id.nav_workouts) {
                selectedFragment = new TODO_Workouts();
            } else if (item.getItemId() == R.id.nav_settings) {
                selectedFragment = new TODO_Settings();
            }

            // Замена текущего фрагмента на выбранный

            if (selectedFragment == null) {
                Toast.makeText(AppNavMenu.this, "Fragment null, return", Toast.LENGTH_LONG).show();
                return false;
            }

            final FragmentManager supportFragment = getSupportFragmentManager();
            final FragmentTransaction transaction = supportFragment.beginTransaction();

            transaction.replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        };
}
