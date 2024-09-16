package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Timers extends Decoration_BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.timers, container, false);

        final ImageButton customTimers = view.findViewById(R.id.customTimersIcon);
        customTimers.setOnClickListener(v -> {
            replaceFragment(new Timers_CustomTimers(), R.id.fragment_container);
        });

        final ImageButton templateTimers = view.findViewById(R.id.templateTimersIcon);
        templateTimers.setOnClickListener(v -> {
            replaceFragment(new Timers_TemplateTimers(), R.id.fragment_container);
        });

        return view;
    }
}
