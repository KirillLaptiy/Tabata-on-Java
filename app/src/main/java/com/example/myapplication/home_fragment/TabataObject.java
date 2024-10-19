package com.example.myapplication.home_fragment;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class TabataObject {
    private Map<String, String> settings;

    public TabataObject(Map<String, String> settings) {
        this.settings = settings;
    }

    // Save Timer Settings to string

    public String toSerializedString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return stringBuilder.toString();
    }

    // Load Timer Settings from string

    @NonNull
    @Contract("_ -> new")
    public static TabataObject fromSerializedString(@NonNull String serializedString) {
        Map<String, String> settings = new HashMap<>();
        String[] pairs = serializedString.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                settings.put(keyValue[0], keyValue[1]);
            }
        }
        return new TabataObject(settings);
    }

    // Gets

/*    Now you can get setting value:
    name = settings.get(timerName);
    warmUpMinutes = settings.get(warmUpMinutes);
    warmUpSeconds = settings.get(warmUpSeconds);
    ... */

    public String getName() {
        return settings.get("name");
    }

    public int getIntervalMinutes() {
        return Integer.parseInt(settings.get("intervalMinutes"));
    }

    public int getIntervalSeconds() {
        return Integer.parseInt(settings.get("intervalSeconds"));
    }

    public int getRestMinutes() {
        return Integer.parseInt(settings.get("restMinutes"));
    }

    public int getRestSeconds() {
        return Integer.parseInt(settings.get("restSeconds"));
    }

    public int getRounds() {
        return Integer.parseInt(settings.get("rounds"));
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    // Sets

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }
}