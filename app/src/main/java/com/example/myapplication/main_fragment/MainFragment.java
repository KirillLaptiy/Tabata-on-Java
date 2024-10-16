package com.example.myapplication.main_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.CustomFragment;
import com.example.myapplication.R;

public class MainFragment extends CustomFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton tabata = view.findViewById(R.id.tabataImage);
        tabata.setOnClickListener(v -> {
            replaceFragment(new TabataFragment(), R.id.main_fragments);

            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("");
            toolbar.setVisibility(View.VISIBLE);
        });

        ImageButton workouts = view.findViewById(R.id.workoutsImage);
        workouts.setOnClickListener(v -> {
            Log.d("MainFragment", "123");
            Toast.makeText(requireContext(), "TODO: Workouts", Toast.LENGTH_LONG).show();
        });
    }
}
