package com.example.myapplication.main_fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.CustomFragment;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TabataFragment extends CustomFragment {
    private List<TabataObject> tabataList;
    private TabataCustomAdapter tabataAdapter;
    private TextView emptyListText;
    protected boolean tabataDialog = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tabata_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emptyListText = view.findViewById(R.id.empty_list_text);

        tabataList = new ArrayList<>();
        tabataAdapter = new TabataCustomAdapter(requireContext(), tabataList, this);

        ListView listView = view.findViewById(R.id.tabataList);
        listView.setAdapter(tabataAdapter);

        FloatingActionButton addTabata = view.findViewById(R.id.addTabata);
        addTabata.setOnClickListener(v -> addTabataDialog());

        loadItemList();
        updateEmptyListText();
    }

    private void addTabataDialog() {
        // return if dialog is already exist

        if (tabataDialog) {
            return;
        }

        tabataDialog = true;

        // create Dialog

        Dialog dialog = new Dialog(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.tabata_add_dialog, null);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_dialog);
        dialog.setContentView(dialogView);

        dialog.show();

        dialog.setOnDismissListener(dialogInterface -> tabataDialog = false);

        // get Views

        EditText[] tabataSettings = getTabataSettings(dialogView);

        MaterialButton buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        MaterialButton buttonAdd = dialogView.findViewById(R.id.buttonAdd);

        buttonCancel.setOnClickListener(view1 -> dialog.dismiss());

        buttonAdd.setOnClickListener(view1 -> {
            // check all fields are filled

            if (isTabataSettingsEmpty(tabataSettings, dialogView)) {
                return;
            }

            // Set settings to HashMap

            Map<String, String> settings = new HashMap<>();

            for (EditText valueField : tabataSettings) {
                String value = valueField.getText().toString().trim();
                String fieldId = getResources().getResourceEntryName(valueField.getId());

                int fieldType = valueField.getInputType();

                if (fieldType == 2) {
                    value = String.valueOf(Integer.parseInt(value));
                }

                settings.put(fieldId, value);
            }

            // add new Tabata Object

            TabataObject newTabataObject = new TabataObject(settings);

            tabataList.add(newTabataObject);
            tabataAdapter.notifyDataSetChanged();

            saveItemList();
            updateEmptyListText();

            dialog.dismiss();
        });
    }

    // Support methods

    // Update Empty List text

    protected void updateEmptyListText() {
        if (tabataList == null || tabataList.isEmpty()) {
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

        for (TabataObject timer : tabataList) {
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
            tabataList.clear();

            String[] timerStrings = timersListString.split(";");

            for (String timerString : timerStrings) {
                tabataList.add(TabataObject.fromSerializedString(timerString));
            }

            tabataAdapter.notifyDataSetChanged();
        }
    }

    // Get Timer Settings

    protected EditText[] getTabataSettings(View view) {
        EditText tabataName = view.findViewById(R.id.name);
        EditText tabataIntervalMinutes = view.findViewById(R.id.intervalMinutes);
        EditText tabataIntervalSeconds = view.findViewById(R.id.intervalSeconds);
        EditText tabataRestMinutes = view.findViewById(R.id.restMinutes);
        EditText tabataRestSeconds = view.findViewById(R.id.restSeconds);
        EditText tabataRounds = view.findViewById(R.id.rounds);

        return new EditText[]{
                tabataName,
                tabataIntervalMinutes,
                tabataIntervalSeconds,
                tabataRestMinutes,
                tabataRestSeconds,
                tabataRounds
        };
    }

    // Set Timer Settings

    protected boolean isTabataSettingsEmpty(EditText[] tabataSettings, View view) {
        boolean isEmpty = false;

        for (EditText valueField : tabataSettings) {
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

                isEmpty = true;
            } else {
                if (fieldType == 2) {
                    if (Integer.parseInt(value) == 0) {

                        if (Objects.equals(fieldId, "rounds")) {
                            valueField.setError("Must be at least 1 round");
                            isEmpty = true;
                        }

                        if (Objects.equals(fieldId, "intervalSeconds")) {
                            EditText intervalMinutesField = view.findViewById(R.id.intervalMinutes);
                            String intervalMinutes = intervalMinutesField.getText().toString().trim();

                            if (intervalMinutes.isEmpty() || Integer.parseInt(intervalMinutes) == 0) {
                                valueField.setError("Must be at least 1 second");
                                isEmpty = true;
                            }
                        }

                        if (Objects.equals(fieldId, "restSeconds")) {
                            EditText restMinutesView = view.findViewById(R.id.restMinutes);
                            String restMinutes = restMinutesView.getText().toString().trim();

                            if (restMinutes.isEmpty() || Integer.parseInt(restMinutes) == 0) {
                                valueField.setError("Must be at least 1 second");
                                isEmpty = true;
                            }
                        }
                    } else {
                        if (Objects.equals(fieldId, "intervalMinutes")) {
                            EditText intervalSecondsField = view.findViewById(R.id.intervalSeconds);
                            intervalSecondsField.setError(null);
                        }

                        if (Objects.equals(fieldId, "restMinutes")) {
                            EditText intervalSecondsField = view.findViewById(R.id.restSeconds);
                            intervalSecondsField.setError(null);
                        }
                    }
                }
            }
        }

        return isEmpty;
    }
}