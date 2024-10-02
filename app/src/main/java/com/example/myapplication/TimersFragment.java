package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TimersFragment extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timers_fragment, container, false);

        ImageButton customTimers = view.findViewById(R.id.customTimersIcon);
        customTimers.setOnClickListener(v -> {
            replaceFragment(new CustomTimersFragment(), R.id.main_fragments);
        });

        ImageButton templateTimers = view.findViewById(R.id.templateTimersIcon);
        templateTimers.setOnClickListener(v -> {
            replaceFragment(new TemplateTimersFragment(), R.id.main_fragments);
        });

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.VISIBLE);

        return view;
    }
}
