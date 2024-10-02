package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.StringRes;

import java.util.Map;

public class TimerRunFragment extends CustomFragment {
    private final Map<String, String> timerSettings;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private String[] stages;
    private int currentStageIndex = 0;
    private int currentStageTime = 0;
    private int currentStageRound = 0;
    private boolean isPaused = false;
    private Button buttonRun;
    private Button buttonPause;
    private Button buttonStop;

    TimerRunFragment(TimerObject timerObject) {
        this.timerSettings = timerObject.getSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timers_run_fragment, container, false);

        for (Map.Entry<String, String> entry : timerSettings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            int viewField = getResources().getIdentifier(key, "id", requireContext().getPackageName());

            if (viewField != 0) {
                TextView valueField = view.findViewById(viewField);
                valueField.setText(value);
            } else {
                throw new IllegalArgumentException("View with key: " + key + " not found.");
            }
        }

        // Get Timer Stages

        int[] stageDurations = getStageDurations(timerSettings);

        // Get Buttons

        ImageButton back = view.findViewById(R.id.timers_button_back);
        back.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        buttonRun = view.findViewById(R.id.buttonRun);
        buttonPause = view.findViewById(R.id.buttonPause);
        buttonStop = view.findViewById(R.id.buttonStop);

        TextView currentStateName = view.findViewById(R.id.currentStateName);
        TextView currentStateTime = view.findViewById(R.id.currentStateTime);
        TextView currentStateRound = view.findViewById(R.id.currentRound);

        // Set first stage

        stages = new String[]{"Warm-up", "Work Interval", "Rest Interval", "Cool down"};
        currentStageIndex = 0;
        currentStageTime = stageDurations[currentStageIndex];
        currentStageRound = Integer.parseInt(timerSettings.get("rounds"));

        currentStateName.setText(stages[currentStageIndex]);
        currentStateTime.setText(String.valueOf(currentStageTime));
        currentStateRound.setText(String.valueOf(currentStageRound));

        buttonRun.setOnClickListener(view1 -> {
            buttonPause.setVisibility(View.VISIBLE);
            buttonStop.setVisibility(View.VISIBLE);
            buttonRun.setVisibility(View.INVISIBLE);

            startTimer(currentStateName, currentStateTime, currentStateRound, stageDurations);
        });

        buttonPause.setOnClickListener(view1 -> {
            if (!isPaused) {
                isPaused = true;
                buttonPause.setText("Unpause");

                timerHandler.removeCallbacks(timerRunnable);
            } else {
                isPaused = false;
                buttonPause.setText("Pause");

                startTimer(currentStateName, currentStateTime, currentStateRound, stageDurations);
            }
        });

        buttonStop.setOnClickListener(view1 -> {
            isPaused = false;
            buttonPause.setText("Pause");
            buttonStop.setText("Stop");

            buttonPause.setVisibility(View.GONE);
            buttonStop.setVisibility(View.GONE);
            buttonRun.setVisibility(View.VISIBLE);

            timerHandler.removeCallbacks(timerRunnable);

            currentStageIndex = 0;
            currentStageTime = stageDurations[currentStageIndex];
            currentStageRound = Integer.parseInt(timerSettings.get("rounds"));

            currentStateName.setText(stages[currentStageIndex]);
            currentStateTime.setText(String.valueOf(currentStageTime));
            currentStateRound.setText(String.valueOf(currentStageRound));
        });

        return view;
    }

    private void startTimer(TextView currentStateName, TextView currentStateTime, TextView currentRound, int[] stageDurations) {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentStageTime > 0) {
                    // Update stage time

                    currentStageTime--;
                    currentStateTime.setText(String.valueOf(currentStageTime));

                    timerHandler.postDelayed(this, 1000);
                } else {
                    // Swap stage

                    currentStageIndex++;

                    if (currentStageIndex < stages.length) {
                        currentStageTime = stageDurations[currentStageIndex];

                        currentStateName.setText(stages[currentStageIndex]);
                        currentStateTime.setText(String.valueOf(currentStageTime));

                        timerHandler.postDelayed(this, 1000);
                    } else {
                        // Swap round

                        currentStageRound--;

                        if (currentStageRound > 0) {
                            currentStageIndex = 0;
                            currentStageTime = stageDurations[currentStageIndex];

                            currentStateName.setText(stages[currentStageIndex]);
                            currentStateTime.setText(String.valueOf(currentStageTime));

                            currentRound.setText(String.valueOf(currentStageRound));

                            timerHandler.postDelayed(this, 1000);
                        } else {
                            buttonPause.setVisibility(View.GONE);
                            buttonRun.setVisibility(View.GONE);
                            buttonStop.setText("Restart");

                            currentRound.setText("0");
                            currentStateName.setText("Finished");
                        }
                    }
                }
            }
        };

        timerHandler.postDelayed(timerRunnable, 1000); // Запускаем таймер через 1 секунду
    }

    private int[] getStageDurations(Map<String, String> timerSettings) {
        String warmUpMinutes = timerSettings.get("warmUpMinutes");
        String warmUpSeconds = timerSettings.get("warmUpSeconds");

        String workIntervalMinutes = timerSettings.get("workIntervalMinutes");
        String workIntervalSeconds = timerSettings.get("workIntervalSeconds");

        String restIntervalMinutes = timerSettings.get("restIntervalMinutes");
        String restIntervalSeconds = timerSettings.get("restIntervalSeconds");

        String coolDownMinutes = timerSettings.get("coolDownMinutes");
        String coolDownSeconds = timerSettings.get("coolDownSeconds");

        return new int[]{
                Integer.parseInt(warmUpMinutes) * 60 + Integer.parseInt(warmUpSeconds),
                Integer.parseInt(workIntervalMinutes) * 60 + Integer.parseInt(workIntervalSeconds),
                Integer.parseInt(restIntervalMinutes) * 60 + Integer.parseInt(restIntervalSeconds),
                Integer.parseInt(coolDownMinutes) * 60 + Integer.parseInt(coolDownSeconds)
        };
    }
}