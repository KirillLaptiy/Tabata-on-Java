package com.example.myapplication;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class TimerObject {
    private Map<String, String> settings;

    public TimerObject(Map<String, String> settings) {
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
    public static TimerObject fromSerializedString(@NonNull String serializedString) {
        Map<String, String> settings = new HashMap<>();
        String[] pairs = serializedString.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                settings.put(keyValue[0], keyValue[1]);
            }
        }
        return new TimerObject(settings);
    }

    // Gets

/*    Now you can get setting value:
    name = settings.get(timerName);
    warmUpMinutes = settings.get(warmUpMinutes);
    warmUpSeconds = settings.get(warmUpSeconds);
    ... */


    public Map<String, String> getSettings() {
        return settings;
    }

    // Sets

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }
}