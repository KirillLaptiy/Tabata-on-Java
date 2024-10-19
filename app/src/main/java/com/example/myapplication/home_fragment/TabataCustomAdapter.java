package com.example.myapplication.home_fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TabataCustomAdapter extends ArrayAdapter<TabataObject> {
    private final List<TabataObject> tabataList;
    private final Context context;
    private final TabataFragment fragment;
    private boolean editDialogVisible = false;

    public TabataCustomAdapter(Context context, List<TabataObject> tabataList, TabataFragment tabataFragment) {
        super(context, R.layout.tabata_custom_adapter, tabataList);
        this.tabataList = tabataList;
        this.context = context;
        this.fragment = tabataFragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.tabata_custom_adapter, parent, false);
        }

        // get Tabata Object

        TabataObject tabataObject = tabataList.get(position);

        // Set Tabata Settings

        TextView tabataName = convertView.findViewById(R.id.tabataName);
        TextView tabataSettings = convertView.findViewById(R.id.tabataSettings);

        tabataName.setText(tabataObject.getName());

        SpannableStringBuilder builder = new SpannableStringBuilder();

        int workoutSeconds = tabataObject.getIntervalMinutes() * 60 + tabataObject.getIntervalSeconds();
        String workoutTime = String.format(Locale.getDefault(), "%02d:%02d", workoutSeconds / 60, workoutSeconds % 60);

        addFormattedText(builder, "Workout: ", workoutTime);
        builder.append("\n");

        int restSeconds = tabataObject.getRestMinutes() * 60 + tabataObject.getRestSeconds();
        String restTime = String.format(Locale.getDefault(), "%02d:%02d", restSeconds / 60, restSeconds % 60);

        addFormattedText(builder, "Rest: ", restTime);
        builder.append("\n");

        addFormattedText(builder, "Rounds: ", String.valueOf(tabataObject.getRounds()));

        tabataSettings.setText(builder);


        // set Timer Buttons

        ImageButton tabataEdit = convertView.findViewById(R.id.tabataEdit);

        tabataEdit.setFocusable(false);
        tabataEdit.setFocusableInTouchMode(false);

        // Edit Button Listener

        tabataEdit.setOnClickListener(view -> {
            if (editDialogVisible || fragment.tabataDialog) {
                return;
            }

            editTabata(tabataObject);
        });

        // Tabata Object Listener

        convertView.setOnClickListener(view -> {
            if (editDialogVisible || fragment.tabataDialog) {
                return;
            }

            fragment.replaceFragment(new TabataRunFragment(tabataObject, this, position), R.id.main_fragments);
        });

        return convertView;
    }

    private void editTabata(TabataObject tabataObject) {
        editDialogVisible = true;

        Dialog dialog = new Dialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.tabata_edit_dialog, null);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.rounded_dialog);
        dialog.setContentView(dialogView);

        dialog.show();

        dialog.setOnDismissListener(dialogInterface -> editDialogVisible = false);

        // Get setting fields

        EditText[] tabataSettingsFields = fragment.getTabataSettings(dialogView);
        Map<String, String> tabataSettings = tabataObject.getSettings();

        // Set start settings

        for (EditText valueField : tabataSettingsFields) {
            String value = dialogView.getResources().getResourceEntryName(valueField.getId());
            String settingValue = tabataSettings.get(value);

            valueField.setHint(settingValue);
        }

        // close dialog button

        MaterialButton cancelEdit = dialogView.findViewById(R.id.buttonCancel);
        cancelEdit.setOnClickListener(view2 -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        });

        // Save new settings

        MaterialButton saveChanges = dialogView.findViewById(R.id.buttonSave);
        saveChanges.setOnClickListener(view2 -> {
            if (fragment.isTabataSettingsEmpty(tabataSettingsFields, dialogView)) {
                return;
            }

            Map<String, String> newSettings = new HashMap<>();

            for (EditText valueField : tabataSettingsFields) {
                String value = valueField.getText().toString().trim();
                String fieldId = context.getResources().getResourceEntryName(valueField.getId());

                int fieldType = valueField.getInputType();

                if (fieldType == 2) {
                    value = String.valueOf(Integer.parseInt(value));
                }

                newSettings.put(fieldId, value);
            }

            tabataObject.setSettings(newSettings);
            notifyDataSetChanged();

            fragment.saveItemList();
            fragment.updateEmptyListText();

            dialog.dismiss();
        });
    }

    private void addFormattedText(SpannableStringBuilder builder, String label, String value) {
        String fullText = label + value;

        builder.append(fullText);
        builder.setSpan(new ForegroundColorSpan(Color.GRAY), builder.length() - value.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    public void deleteTabata(int position){
        if (editDialogVisible || fragment.tabataDialog) {
            return;
        }

        tabataList.remove(position);
        notifyDataSetChanged();

        fragment.saveItemList();
        fragment.updateEmptyListText();
    }
}