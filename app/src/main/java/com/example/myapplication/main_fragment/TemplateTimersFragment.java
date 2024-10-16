package com.example.myapplication.main_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.CustomFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class TemplateTimersFragment extends CustomFragment {
    private final Map<String, String> beginnerSettings = new HashMap<>();
    private final Map<String, String> intermediateSettings = new HashMap<>();
    private final Map<String, String> advancedSettings = new HashMap<>();

    {
        new CustomFragment() {
            @Override
            public void replaceFragment(Fragment fragment, int containerId) {
                super.replaceFragment(fragment, containerId);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Возвращает разметку фрагмента
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.GONE);

        return inflater.inflate(R.layout.timers_template_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton back = view.findViewById(R.id.timers_button_back);
        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        createTimersSettings();

        TabataObject beginnerTimer = new TabataObject(beginnerSettings);
        TabataObject intermediateTimer = new TabataObject(intermediateSettings);
        TabataObject advancedTimer = new TabataObject(advancedSettings);

        LinearLayout beginnerButton = view.findViewById(R.id.beginnerButton);
        LinearLayout intermediateButton = view.findViewById(R.id.intermediateButton);
        LinearLayout advancedButton = view.findViewById(R.id.advancedButton);

        setTimerSettings(beginnerButton, beginnerSettings);
        setTimerSettings(intermediateButton, intermediateSettings);
        setTimerSettings(advancedButton, advancedSettings);

//        beginnerButton.setOnClickListener(v -> {
//            replaceFragment(new TabataRunFragment(beginnerTimer), R.id.main_fragments);
//        });
//
//        intermediateButton.setOnClickListener(v -> {
//            replaceFragment(new TabataRunFragment(intermediateTimer), R.id.main_fragments);
//        });
//
//        advancedButton.setOnClickListener(v -> {
//            replaceFragment(new TabataRunFragment(advancedTimer), R.id.main_fragments);
//        });
    }

    private void createTimersSettings() {
        beginnerSettings.put("timerName", "Beginner Tabata");
        beginnerSettings.put("warmUpMinutes", "5");
        beginnerSettings.put("warmUpSeconds", "0");
        beginnerSettings.put("workIntervalMinutes", "0");
        beginnerSettings.put("workIntervalSeconds", "20");
        beginnerSettings.put("restIntervalMinutes", "0");
        beginnerSettings.put("restIntervalSeconds", "10");
        beginnerSettings.put("coolDownMinutes", "5");
        beginnerSettings.put("coolDownSeconds", "0");
        beginnerSettings.put("rounds", "4");

        intermediateSettings.put("timerName", "Intermediate Tabata");
        intermediateSettings.put("warmUpMinutes", "5");
        intermediateSettings.put("warmUpSeconds", "0");
        intermediateSettings.put("workIntervalMinutes", "0");
        intermediateSettings.put("workIntervalSeconds", "30");
        intermediateSettings.put("restIntervalMinutes", "0");
        intermediateSettings.put("restIntervalSeconds", "15");
        intermediateSettings.put("coolDownMinutes", "5");
        intermediateSettings.put("coolDownSeconds", "0");
        intermediateSettings.put("rounds", "6");

        advancedSettings.put("timerName", "Advanced Tabata");
        advancedSettings.put("warmUpMinutes", "5");
        advancedSettings.put("warmUpSeconds", "0");
        advancedSettings.put("workIntervalMinutes", "0");
        advancedSettings.put("workIntervalSeconds", "40");
        advancedSettings.put("restIntervalMinutes", "0");
        advancedSettings.put("restIntervalSeconds", "20");
        advancedSettings.put("coolDownMinutes", "5");
        advancedSettings.put("coolDownSeconds", "0");
        advancedSettings.put("rounds", "8");
    }

    private void setTimerSettings(LinearLayout button, Map<String, String> settings) {
        ((TextView) button.findViewWithTag("timerName")).setText(settings.get("timerName"));
        ((TextView) button.findViewWithTag("warmUpMinutes")).setText("Warm-up: " + settings.get("warmUpMinutes") + " m " + settings.get("warmUpSeconds") + " s");
        ((TextView) button.findViewWithTag("workIntervalMinutes")).setText("Work Interval: " + settings.get("workIntervalMinutes") + " m " + settings.get("workIntervalSeconds") + " s");
        ((TextView) button.findViewWithTag("restIntervalMinutes")).setText("Rest Interval: " + settings.get("restIntervalMinutes") + " m " + settings.get("restIntervalSeconds") + " s");
        ((TextView) button.findViewWithTag("coolDownMinutes")).setText("Cool-down: " + settings.get("coolDownMinutes") + " m " + settings.get("coolDownSeconds") + " s");
        ((TextView) button.findViewWithTag("rounds")).setText("Rounds: " + settings.get("rounds"));
    }
}