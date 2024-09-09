package com.example.myapplication;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HuyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Разметка фрагмента
        final View view = inflater.inflate(R.layout.fragment_huy, container, false);

        final Button randomizer = view.findViewById(R.id.randomizer);
        randomizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вывод случайного числа
                int randomNum = (int) (Math.random() * 1000) + 1;
                Toast.makeText(getActivity(), "Random Number: " + randomNum, Toast.LENGTH_SHORT).show();
            }
        });

        final Button sound = view.findViewById(R.id.nahrena);

        SoundPool soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .build();

        int soundId = soundPool.load(getActivity(), R.raw.nahrena, 1);

        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Воспроизведение звука
                soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });

        final ImageButton star = view.findViewById(R.id.imageStar);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вывод случайного числа
                Toast.makeText(getActivity(), "Star: " + 1, Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton add = view.findViewById(R.id.imageAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вывод случайного числа
                Toast.makeText(getActivity(), "Add: " + 2, Toast.LENGTH_SHORT).show();
            }
        });

        final ImageButton delete = view.findViewById(R.id.imageDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вывод случайного числа
                Toast.makeText(getActivity(), "Delete: " + 3, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
