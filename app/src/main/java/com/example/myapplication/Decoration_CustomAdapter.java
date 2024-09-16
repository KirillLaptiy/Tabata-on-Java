package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class Decoration_CustomAdapter extends ArrayAdapter<Timers_Item> {
    private final List<Timers_Item> items;
    private final Context context;
    private final Timers_CustomTimers fragment;

    public Decoration_CustomAdapter(Context context, List<Timers_Item> items, Timers_CustomTimers customTimers) {
        super(context, R.layout.timers_custom_array_adapter, items);
        this.items = items;
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

        final TextView itemText = convertView.findViewById(R.id.itemText);
        final ImageButton deleteButton = convertView.findViewById(R.id.timers_button_delete);
        final ImageButton editButton = convertView.findViewById(R.id.timers_button_edit);

        deleteButton.setFocusable(false);
        deleteButton.setFocusableInTouchMode(false);

        editButton.setFocusable(false);
        editButton.setFocusableInTouchMode(false);

        // Получаем объект таймера
        final Timers_Item timerItem = items.get(position);

        // Устанавливаем только имя таймера
        itemText.setText(timerItem.getName());

        // Обрабатываем нажатие на кнопку Remove
        deleteButton.setOnClickListener(v -> {
            items.remove(position);
            notifyDataSetChanged();
            this.fragment.saveItemList();
            this.fragment.updateEmptyListText();
        });

        editButton.setOnClickListener(v -> {
            this.fragment.createTimerEditDialog(position);
        });

        return convertView;
    }
}


