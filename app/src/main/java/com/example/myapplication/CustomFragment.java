package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class CustomFragment extends Fragment {
    public void replaceFragment(Fragment fragment, int containerId) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
