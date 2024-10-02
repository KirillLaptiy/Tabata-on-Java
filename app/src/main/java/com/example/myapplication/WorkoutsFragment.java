package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WorkoutsFragment extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Возвращает разметку фрагмента

        return inflater.inflate(R.layout.workouts_fragment, container, false);
    }
}
