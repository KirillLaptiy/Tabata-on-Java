package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Timers_CustomTimers extends Decoration_BaseFragment {
    private List<Timers_Item> itemList = new ArrayList<>();
    private Decoration_CustomAdapter adapter;
    private TextView emptyListText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Возвращает разметку фрагмента
        final View view = inflater.inflate(R.layout.timers_custom, container, false);

        emptyListText = view.findViewById(R.id.empty_list_text);

        itemList = new ArrayList<>();
        adapter = new Decoration_CustomAdapter(requireContext(), itemList, this);

        final ListView listView = view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

        final ImageButton back = view.findViewById(R.id.timers_button_back);
        back.setOnClickListener(v -> {
            replaceFragment(new Timers(), R.id.fragment_container);
        });

        final ImageButton addNewTimer = view.findViewById(R.id.timers_button_add);
        addNewTimer.setOnClickListener(v -> createNewTimer());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadItemList();
        setTimerClickListener();
        updateEmptyListText();
    }

    void updateEmptyListText() {
        if (itemList == null || itemList.isEmpty()) {
            emptyListText.setVisibility(View.VISIBLE);
        } else {
            emptyListText.setVisibility(View.GONE);
        }
    }
    void saveItemList() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        StringBuilder stringBuilder = new StringBuilder();
        for (Timers_Item timer : itemList) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(timer.toString());  // Сохраняем полные данные таймера
        }

        editor.putString("itemList", stringBuilder.toString());
        editor.apply();
    }

    private void loadItemList() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String itemListString = sharedPreferences.getString("itemList", "");

        if (!itemListString.isEmpty()) {
            itemList.clear();
            for (String itemString : itemListString.split(",")) {
                itemList.add(Timers_Item.fromString(itemString));
            }
            adapter.notifyDataSetChanged();
        }
    }


    private void createNewTimer() {
        // return Dialog

        final Decoration_DialogResult dialogResult = addNewDialog(R.layout.timers_custom_timer_new_dialog);

        final View dialogView = dialogResult.getDialogView();
        final AlertDialog dialog = dialogResult.getDialog();

        // find Views
        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        final Spinner spinnerWarmUp = dialogView.findViewById(R.id.spinnerWarmUp);
        final Spinner spinnerWorkInterval = dialogView.findViewById(R.id.spinnerWorkInterval);
        final Spinner spinnerRestInterval = dialogView.findViewById(R.id.spinnerRestInterval);
        final Spinner spinnerRounds = dialogView.findViewById(R.id.spinnerRounds);
        final Spinner spinnerCoolDown = dialogView.findViewById(R.id.spinnerCoolDown);

        final Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        final Button buttonAdd = dialogView.findViewById(R.id.buttonAdd);

        // init Values

        ArrayAdapter<Integer> adapterMinutes = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 10));
        ArrayAdapter<Integer> adapterSeconds = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(10, 15, 20, 30, 40, 60));
        ArrayAdapter<Integer> adapterRounds = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 10));

        // set Values

        spinnerWarmUp.setAdapter(adapterMinutes);
        spinnerWorkInterval.setAdapter(adapterSeconds);
        spinnerRestInterval.setAdapter(adapterSeconds);
        spinnerRounds.setAdapter(adapterRounds);
        spinnerCoolDown.setAdapter(adapterMinutes);

        // set Button Listeners

        buttonCancel.setOnClickListener(view1 -> dialog.dismiss());

        buttonAdd.setOnClickListener(view1 -> {
            final String name = editTextName.getText().toString().trim();

            final int warmUp = (int) spinnerWarmUp.getSelectedItem();
            final int workInterval = (int) spinnerWorkInterval.getSelectedItem();
            final int restInterval = (int) spinnerRestInterval.getSelectedItem();
            final int rounds = (int) spinnerRounds.getSelectedItem();
            final int coolDown = (int) spinnerCoolDown.getSelectedItem();

            if (!name.isEmpty()) {

                Timers_Item timerItem = new Timers_Item(name, warmUp, workInterval, restInterval, rounds, coolDown);

                itemList.add(Timers_Item.fromString(String.valueOf(timerItem)));

                // Уведомляем адаптер об изменениях
                adapter.notifyDataSetChanged();

                saveItemList();
                setTimerClickListener();
                updateEmptyListText(); // Проверяем, пустой ли список

                dialog.dismiss();
            } else {
                editTextName.setError("Name is required");
            }
        });
    }

    void createTimerEditDialog(int position) {
        final Decoration_DialogResult editDialogCreate = addNewDialog(R.layout.timers_custom_timer_edit_dialog);

        final View editDialogView = editDialogCreate.getDialogView();
        final AlertDialog EditDialog = editDialogCreate.getDialog();

        final ImageButton closeEditDialog = editDialogView.findViewById(R.id.timers_button_closeDialog);
        final ImageButton deleteTimer = editDialogView.findViewById(R.id.timers_button_delete);

        // Получаем таймер, который будем редактировать
        final Timers_Item selectedTimer = itemList.get(position);

        // Получаем поля для редактирования
        final EditText editTimerName = editDialogView.findViewById(R.id.editTimerName);
        final Spinner spinnerWarmUp = editDialogView.findViewById(R.id.spinnerWarmUp);
        final Spinner spinnerWorkInterval = editDialogView.findViewById(R.id.spinnerWorkInterval);
        final Spinner spinnerRestInterval = editDialogView.findViewById(R.id.spinnerRestInterval);
        final Spinner spinnerRounds = editDialogView.findViewById(R.id.spinnerRounds);
        final Spinner spinnerCoolDown = editDialogView.findViewById(R.id.spinnerCoolDown);

        // set Name

        editTimerName.setText(selectedTimer.getName());
        editTimerName.setHint(selectedTimer.getName());

        // init Values

        ArrayAdapter<Integer> adapterMinutes = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 10));
        ArrayAdapter<Integer> adapterSeconds = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(10, 15, 20, 30, 40, 60));
        ArrayAdapter<Integer> adapterRounds = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Arrays.asList(1, 2, 3, 4, 5, 10));

        // set Adapters

        spinnerWarmUp.setAdapter(adapterMinutes);
        spinnerWorkInterval.setAdapter(adapterSeconds);
        spinnerRestInterval.setAdapter(adapterSeconds);
        spinnerCoolDown.setAdapter(adapterMinutes);
        spinnerRounds.setAdapter(adapterRounds);

        // set Values

        spinnerWarmUp.setSelection(adapterMinutes.getPosition(selectedTimer.getWarmUp()));
        spinnerWorkInterval.setSelection(adapterSeconds.getPosition(selectedTimer.getWorkInterval()));
        spinnerRestInterval.setSelection(adapterSeconds.getPosition(selectedTimer.getRestInterval()));
        spinnerCoolDown.setSelection(adapterMinutes.getPosition(selectedTimer.getCoolDown()));
        spinnerRounds.setSelection(adapterRounds.getPosition(selectedTimer.getRounds()));

        final Button saveChanges = editDialogView.findViewById(R.id.buttonSave);

        closeEditDialog.setOnClickListener(view2 -> {
            if (EditDialog != null && EditDialog.isShowing()) {
                EditDialog.dismiss();
            }
        });

        deleteTimer.setOnClickListener(view2 -> {
            // Check item != null

            if (position >= itemList.size()) {
                EditDialog.dismiss();
                return;
            }

            itemList.remove(position);
            adapter.notifyDataSetChanged();

            saveItemList();
            updateEmptyListText();

            EditDialog.dismiss();
        });

        saveChanges.setOnClickListener(view2 -> {
            // Check item != null

            if (position >= itemList.size()) {
                EditDialog.dismiss();
                return;
            }

            String newName = editTimerName.getText().toString();

            // Получаем новые значения из Spinner'ов
            int newWarmUp = (int) spinnerWarmUp.getSelectedItem();
            int newWorkInterval = (int) spinnerWorkInterval.getSelectedItem();
            int newRestInterval = (int) spinnerRestInterval.getSelectedItem();
            int newRounds = (int) spinnerRounds.getSelectedItem();
            int newCoolDown = (int) spinnerCoolDown.getSelectedItem();

            // Обновляем таймер, если новое имя не пустое
            if (!newName.isEmpty()) {
                Timers_Item updatedTimer = new Timers_Item(newName, newWarmUp, newWorkInterval, newRestInterval, newRounds, newCoolDown);
                itemList.set(position, updatedTimer);
                adapter.notifyDataSetChanged();

                saveItemList();  // Сохраняем изменения

                EditDialog.dismiss();
            } else {
                editTimerName.setError("Name is required");
            }
        });
    }

    private void setTimerClickListener() {
        // set Timer Listener

        final ListView listView = requireView().findViewById(R.id.listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Timers_Item selectedTimer = itemList.get(position);

            Decoration_DialogResult infoDialogCreate = addNewDialog(R.layout.timers_custom_timer_info_dialog);
            View dialogView = infoDialogCreate.getDialogView();
            AlertDialog dialog = infoDialogCreate.getDialog();

            TextView name = dialogView.findViewById(R.id.timerName);
            TextView warmUp = dialogView.findViewById(R.id.timerWarmUp);
            TextView workInterval = dialogView.findViewById(R.id.timerWorkInterval);
            TextView restInterval = dialogView.findViewById(R.id.timerRestInterval);
            TextView rounds = dialogView.findViewById(R.id.timerRounds);
            TextView coolDown = dialogView.findViewById(R.id.timerCoolDown);

            name.setText(getString(R.string.name_format, selectedTimer.getName()));
            warmUp.setText(getString(R.string.warm_up_format, selectedTimer.getWarmUp()));
            workInterval.setText(getString(R.string.work_interval_format, selectedTimer.getWorkInterval()));
            restInterval.setText(getString(R.string.rest_interval_format, selectedTimer.getRestInterval()));
            rounds.setText(getString(R.string.rounds_format, selectedTimer.getRounds()));
            coolDown.setText(getString(R.string.cool_down_format, selectedTimer.getCoolDown()));

            final ImageButton closeDialog = dialogView.findViewById(R.id.timers_button_closeDialog);
            final ImageButton delete = dialogView.findViewById(R.id.timers_button_delete);

            final Button edit = dialogView.findViewById(R.id.buttonEdit);

            // set Button Listeners

            closeDialog.setOnClickListener(view1 -> dialog.dismiss());

            edit.setOnClickListener(view1 -> {
                // Check item != null

                if (position >= itemList.size()) {
                    dialog.dismiss();
                    return;
                }

                // Dismiss info dialog

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                createTimerEditDialog(position);
            });

            delete.setOnClickListener(view1 -> {
                // Check item != null

                if (position >= itemList.size()) {
                    dialog.dismiss();
                    return;
                }

                // Delete item

                itemList.remove(position);
                adapter.notifyDataSetChanged();

                saveItemList();
                updateEmptyListText();

                dialog.dismiss();
            });
        });
    }

    @NonNull
    @Contract("_ -> new")
    private Decoration_DialogResult addNewDialog(int dialogLayout) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        final View dialogView = LayoutInflater.from(requireContext()).inflate(dialogLayout, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        dialog.show();

        return new Decoration_DialogResult(dialogView, dialog);
    }
}

