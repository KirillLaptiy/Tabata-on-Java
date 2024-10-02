package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class CustomTimersAdapter extends ArrayAdapter<TimerObject> {
    private final List<TimerObject> timersList;
    private final Context context;
    private final CustomTimersFragment fragment;
    private boolean editDialogVisible = false;

    public CustomTimersAdapter(Context context, List<TimerObject> timers, CustomTimersFragment customTimers) {
        super(context, R.layout.timers_custom_array_adapter, timers);
        this.timersList = timers;
        this.context = context;
        this.fragment = customTimers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.timers_custom_array_adapter, parent, false);
        }

        // get Timer Object

        TimerObject timerObject = timersList.get(position);

        TextView nameField = convertView.findViewById(R.id.timerName);

        nameField.setText(timerObject.getSettings().get("timerName"));

        // set Timer Buttons

        ImageButton timerButtonEdit = convertView.findViewById(R.id.timerButtonEdit);
        ImageButton timerButtonDelete = convertView.findViewById(R.id.timerButtonDelete);

        timerButtonEdit.setFocusable(false);
        timerButtonEdit.setFocusableInTouchMode(false);

        timerButtonDelete.setFocusable(false);
        timerButtonDelete.setFocusableInTouchMode(false);

        // Set click listener on the entire view

        convertView.setOnClickListener(view -> {
            if (editDialogVisible) {
                return;
            }

            timerRunFragment(timerObject);
        });

        // set click listeners on the Timer Buttons

        timerButtonEdit.setOnClickListener(view -> {
            if (editDialogVisible || fragment.newTimerDialogVisible) {
                return;
            }

            timerEditDialog(position, timerObject);
        });

        timerButtonDelete.setOnClickListener(view -> {
            if (editDialogVisible || fragment.newTimerDialogVisible) {
                return;
            }

            timersList.remove(position);
            notifyDataSetChanged();

            fragment.saveItemList();
            fragment.updateEmptyListText();
        });

        // return view

        return convertView;
    }

    private void timerRunFragment(TimerObject timerObject) {
        fragment.replaceFragment(new TimerRunFragment(timerObject), R.id.main_fragments);
    }


    private void timerEditDialog(int position, TimerObject timerObject) {
        // return if dialog is already exist

        editDialogVisible = true;

        CustomDialog newDialog = fragment.addNewDialog(R.layout.timers_custom_edit_dialog);

        View dialogView = newDialog.getDialogView();
        AlertDialog dialog = newDialog.getDialog();

        // Get setting fields

        EditText[] timerSettingFields = fragment.getTimerSettings(dialogView);
        Map<String, String> timerSettings = timerObject.getSettings();

        // Set start settings

        for (EditText valueField : timerSettingFields) {
            String editField = dialogView.getResources().getResourceEntryName(valueField.getId());
            String startValue = timerSettings.get(editField);

            valueField.setText(startValue);
            valueField.setHint(startValue);
        }

        // onClose Event

        dialog.setOnDismissListener(dialogInterface -> editDialogVisible = false);

        // close dialog button

        ImageButton closeEditDialog = dialogView.findViewById(R.id.timerButtonClose);
        closeEditDialog.setOnClickListener(view2 -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        // delete Button

        ImageButton deleteTimer = dialogView.findViewById(R.id.timerButtonDelete);
        deleteTimer.setOnClickListener(view2 -> {
            // Check item != null

            if (position >= timersList.size()) {
                dialog.dismiss();
                return;
            }

            timersList.remove(position);
            notifyDataSetChanged();

            fragment.saveItemList();
            fragment.updateEmptyListText();

            dialog.dismiss();
        });

        // Save new settings

        Button saveChanges = dialogView.findViewById(R.id.buttonSave);
        saveChanges.setOnClickListener(view2 -> {

            boolean valueFieldEmpty = fragment.setTimerSettings(timerSettingFields, timerSettings);

            if (valueFieldEmpty) {
                return;
            }

            timerObject.setSettings(timerSettings);
            notifyDataSetChanged();

            fragment.saveItemList();
            fragment.updateEmptyListText();

            dialog.dismiss();
        });
    }
}