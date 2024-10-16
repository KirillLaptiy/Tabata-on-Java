package com.example.myapplication.main_fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Outline;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.CustomFragment;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;
import java.util.Objects;

public class TabataRunFragment extends CustomFragment {
    public static TabataObject tabataObject = null;
    private final TabataCustomAdapter adapter;
    private final int tabataPosition;

    public static String tabataState;

    private ClockView clockView;

    TabataRunFragment(TabataObject timerObject, TabataCustomAdapter adapter, int tabataPosition) {
        tabataObject = timerObject;
        this.adapter = adapter;
        this.tabataPosition = tabataPosition;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(tabataObject.getName());

        return inflater.inflate(R.layout.tabata_run_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Get Views

        clockView = view.findViewById(R.id.clockView);

        TextView currentStageTime = view.findViewById(R.id.stageTimeView);
        TextView currentStageRound = view.findViewById(R.id.stageRoundView);

        int totalSeconds = TabataRunFragment.tabataObject.getIntervalMinutes() * 60 + TabataRunFragment.tabataObject.getIntervalSeconds();
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", totalSeconds / 60, totalSeconds % 60);
        currentStageTime.setText(formattedTime);

        String hexColor = "#00C2FF";
        int customColor = Color.parseColor(hexColor);

        SpannableString spannable = new SpannableString("1 / " + tabataObject.getRounds());
        spannable.setSpan(new ForegroundColorSpan(customColor), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        currentStageRound.setText(spannable);

        // Get Buttons

        MaterialButton deleteButton = view.findViewById(R.id.tabataDelete);
        MaterialButton runButton = view.findViewById(R.id.tabataRun);
        MaterialButton restartButton = view.findViewById(R.id.tabataRestart);

        deleteButton.setOnClickListener(v -> {

            Dialog tabataDeleteDialog = new Dialog(requireContext());
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.tabata_delete_dialog, null);

            Objects.requireNonNull(tabataDeleteDialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_dialog);
            tabataDeleteDialog.setContentView(dialogView);

            tabataDeleteDialog.show();

            MaterialButton cancelDelete = dialogView.findViewById(R.id.buttonCancel);
            MaterialButton deleteTabata = dialogView.findViewById(R.id.buttonDelete);

            cancelDelete.setOnClickListener(v1 -> tabataDeleteDialog.dismiss());

            deleteTabata.setOnClickListener(v1 -> {
                tabataDeleteDialog.dismiss();
                adapter.deleteTabata(tabataPosition);
                requireActivity().getSupportFragmentManager().popBackStack();
            });
        });

        runButton.setOnClickListener(v -> {
            boolean isButtonsClickable = true;

            if (tabataState == null) {
                isButtonsClickable = false;

                tabataState = "Started";
                runButton.setIconResource(R.drawable.tabata_paused);


                long workInterval = (tabataObject.getIntervalMinutes() * 60L + tabataObject.getIntervalSeconds()) * 1000L;
                clockView.startTimer(workInterval, workInterval);
            } else if (tabataState.equals("Started")) {
                tabataState = "Paused";
                runButton.setIconResource(R.drawable.tabata_run);
                clockView.pauseTimer();
            } else if (tabataState.equals("Paused")) {
                isButtonsClickable = false;

                tabataState = "Started";
                runButton.setIconResource(R.drawable.tabata_paused);
                clockView.resumeTimer();
            }

            setButtonsClickable(view, isButtonsClickable);
        });

        restartButton.setOnClickListener(v -> {
            clockView.stopTimer(true);
            tabataState = null;

            setButtonsClickable(view, true);
        });
    }

    public static void setButtonsClickable(View view, boolean isClickable) {
        view.findViewById(R.id.tabataDelete).setClickable(isClickable);
        view.findViewById(R.id.tabataRestart).setClickable(isClickable);
    }

    @Override
    public void onDestroyView() {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");

        clockView.stopTimer(true);

        requireView().findViewById(R.id.tabataDelete).setClickable(true);
        requireView().findViewById(R.id.tabataRestart).setClickable(true);

        super.onDestroyView();
    }
}