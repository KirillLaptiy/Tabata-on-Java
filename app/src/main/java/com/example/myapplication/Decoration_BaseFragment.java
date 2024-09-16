package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class Decoration_BaseFragment extends Fragment {
    protected void replaceFragment(Fragment fragment, int containerId) {
        final FragmentManager fragmentManager = getParentFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
