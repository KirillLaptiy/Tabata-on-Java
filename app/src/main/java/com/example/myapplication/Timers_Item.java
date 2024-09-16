package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

public class Timers_Item {
    private final String name;
    private final int warmUp;
    private final int workInterval;
    private final int restInterval;
    private final int rounds;
    private final int coolDown;

    // Конструктор
    public Timers_Item(String name, int warmUp, int workInterval, int restInterval, int rounds, int coolDown) {
        this.name = name;
        this.warmUp = warmUp;
        this.workInterval = workInterval;
        this.restInterval = restInterval;
        this.rounds = rounds;
        this.coolDown = coolDown;
    }

    // Геттеры
    public String getName() { return name; }
    public int getWarmUp() { return warmUp; }
    public int getWorkInterval() { return workInterval; }
    public int getRestInterval() { return restInterval; }
    public int getRounds() { return rounds; }
    public int getCoolDown() { return coolDown; }

    // Сериализация
    @NonNull
    @Override
    public String toString() {
        return name + ";" + warmUp + ";" + workInterval + ";" + restInterval + ";" + rounds + ";" + coolDown;
    }

    // Десериализация
    public static Timers_Item fromString(String serialized) {
        String[] parts = serialized.split(";");

        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid serialized format: " + serialized);
        }

        return new Timers_Item(
                parts[0],
                Integer.parseInt(parts[1]),
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                Integer.parseInt(parts[5])
        );
    }
}

