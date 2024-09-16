package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Timers_TemplateTimers extends Decoration_BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Возвращает разметку фрагмента
        final View view = inflater.inflate(R.layout.timers_template, container, false);

        final ImageButton back = view.findViewById(R.id.timers_button_back);
        back.setOnClickListener(v -> {
            replaceFragment(new Timers(), R.id.fragment_container);
        });

        return view;
    }
}