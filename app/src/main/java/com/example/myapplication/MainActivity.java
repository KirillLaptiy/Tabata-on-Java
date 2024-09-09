package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка первого фрагмента по умолчанию
        replaceFragment(new ActivityLoad());

        final Button buttonHuy = findViewById(R.id.button_huy);
        final Button buttonFirst = findViewById(R.id.button_first);
        final Button buttonSecond = findViewById(R.id.button_second);
        final Button buttonPrint = findViewById(R.id.button_print);

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Заменяем на первый фрагмент
                replaceFragment(new HuyFragment());
            }
        });

        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Заменяем на первый фрагмент
                replaceFragment(new FirstFragment());
            }
        });

        buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Заменяем на второй фрагмент
                replaceFragment(new SecondFragment());
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Some Text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
