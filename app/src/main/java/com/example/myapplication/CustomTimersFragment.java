package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomTimersFragment extends CustomFragment {
    protected boolean newTimerDialogVisible = false;
    private List<TimerObject> timersList;
    private CustomTimersAdapter adapter;
    private TextView emptyListText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
        bottomNav.setVisibility(View.GONE);

        return inflater.inflate(R.layout.timers_custom_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        timersList = new ArrayList<>();
        adapter = new CustomTimersAdapter(requireContext(), timersList, CustomTimersFragment.this);

        emptyListText = view.findViewById(R.id.empty_list_text);

        ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        ImageButton back = view.findViewById(R.id.timers_button_back);
        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        ImageButton addNewTimer = view.findViewById(R.id.timers_button_add);
        addNewTimer.setOnClickListener(v -> createNewTimer());

        loadItemList();
        updateEmptyListText();
    }

    private void createNewTimer() {
        // return if dialog is already exist

        if (newTimerDialogVisible) {
            return;
        }

        newTimerDialogVisible = true;

        // create Dialog

        CustomDialog customDialog = addNewDialog(R.layout.timers_custom_new_dialog);

        View dialogView = customDialog.getDialogView();
        AlertDialog dialog = customDialog.getDialog();

        // get Views

        EditText[] timerSettings = getTimerSettings(dialogView);
        for (EditText valueField : timerSettings) {
            String value = "";

            String valueFieldId = dialogView.getResources().getResourceEntryName(valueField.getId());

            switch (valueFieldId) {
                case "timerName":
                    value = "New Timer";
                    break;
                case "warmUpMinutes":
                case "coolDownMinutes":
                    value = "5";
                    break;
                case "warmUpSeconds":
                case "restIntervalMinutes":
                case "coolDownSeconds":
                case "workIntervalMinutes":
                    value = "0";
                    break;
                case "workIntervalSeconds":
                    value = "20";
                    break;
                case "restIntervalSeconds":
                    value = "15";
                    break;
                case "rounds":
                    value = "6";
                    break;
            }
            valueField.setText(value);
        }

        // set Dialog Button Listeners

        dialog.setOnDismissListener(dialogInterface -> newTimerDialogVisible = false);

        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(view1 -> {
            dialog.dismiss();
        });

        Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(view1 -> {
            // check all fields are filled

            Map<String, String> settings = new HashMap<>();

            boolean valueFieldEmpty = setTimerSettings(timerSettings, settings);

            if (valueFieldEmpty) {
                return;
            }

            // add new Timer

            Log.d("TimerValues", "Settings: " + settings);

            TimerObject newTimerObject = new TimerObject(settings);

            timersList.add(newTimerObject);
            adapter.notifyDataSetChanged();

            saveItemList();
            updateEmptyListText();

            dialog.dismiss();
        });
    }

    // Support methods

    // Update Empty List text

    protected void updateEmptyListText() {
        if (timersList == null || timersList.isEmpty()) {
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
        }
    }

    // Save Timer Object

    protected void saveItemList() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CustomTimersObjects", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder stringBuilder = new StringBuilder();

        for (TimerObject timer : timersList) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(";");
            }

            stringBuilder.append(timer.toSerializedString());
        }

        editor.putString("timersList", stringBuilder.toString());
        editor.apply();
    }

    // Load all Timer Objects

    private void loadItemList() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("CustomTimersObjects", Context.MODE_PRIVATE);
        String timersListString = sharedPreferences.getString("timersList", "");

        if (!timersListString.isEmpty()) {
            timersList.clear();

            String[] timerStrings = timersListString.split(";");

            for (String timerString : timerStrings) {
                timersList.add(TimerObject.fromSerializedString(timerString));
            }

            adapter.notifyDataSetChanged();
        }
    }

    // Get Timer Settings

    protected EditText[] getTimerSettings(View view) {
        EditText name = view.findViewById(R.id.timerName);
        EditText warmUpMinutes = view.findViewById(R.id.warmUpMinutes);
        EditText warmUpSeconds = view.findViewById(R.id.warmUpSeconds);
        EditText workIntervalMinutes = view.findViewById(R.id.workIntervalMinutes);
        EditText workIntervalSeconds = view.findViewById(R.id.workIntervalSeconds);
        EditText restIntervalMinutes = view.findViewById(R.id.restIntervalMinutes);
        EditText restIntervalSeconds = view.findViewById(R.id.restIntervalSeconds);
        EditText coolDownMinutes = view.findViewById(R.id.coolDownMinutes);
        EditText coolDownSeconds = view.findViewById(R.id.coolDownSeconds);
        EditText rounds = view.findViewById(R.id.rounds);

        return new EditText[] {
            name,
            warmUpMinutes,
            warmUpSeconds,
            workIntervalMinutes,
            workIntervalSeconds,
            restIntervalMinutes,
            restIntervalSeconds,
            coolDownMinutes,
            coolDownSeconds,
            rounds
        };
    }

    // Set Timer Settings

    protected boolean setTimerSettings(EditText[] timerSettings, Map<String, String> settings) {
        boolean valueFieldEmpty = false;

        for (EditText valueField : timerSettings) {
            String value = valueField.getText().toString().trim();

            int fieldType = valueField.getInputType();
            String fieldId = getResources().getResourceEntryName(valueField.getId());

            if (value.isEmpty()) {

                if (fieldType == 1) {
                    valueField.setError("Name is required");
                } else {
                    if (Objects.equals(fieldId, "rounds")) {
                        valueField.setError("Rounds are required");
                    } else {
                        valueField.setError("Time is required");
                    }
                }

                valueFieldEmpty = true;
            } else {
                if (fieldType == 2) {
                    value = String.valueOf(Integer.parseInt(value));

                    if (Objects.equals(fieldId, "rounds") && Integer.parseInt(value) == 0) {
                        valueField.setError("Must be at least 1 round");
                        return true;
                    }
                }

                settings.put(fieldId, value);
            }
        }

        return valueFieldEmpty;
    }

    // Create Dialog

    @NonNull
    @Contract("_ -> new")
    protected CustomDialog addNewDialog(int dialogLayout) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        View dialogView = LayoutInflater.from(requireContext()).inflate(dialogLayout, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        return new CustomDialog(dialogView, dialog);
    }
}