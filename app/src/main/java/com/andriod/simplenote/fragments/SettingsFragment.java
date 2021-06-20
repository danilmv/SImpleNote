package com.andriod.simplenote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.simplenote.R;


public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_delete_all).setOnClickListener(v -> {
            if (getController() != null) {
                getController().deleteAll();
            }
        });

        view.findViewById(R.id.button_sign_in).setOnClickListener(v -> {
            if (getController()!=null){
                getController().signIn();
            }
        });

        view.findViewById(R.id.button_sign_out).setOnClickListener(v -> {
            if (getController()!=null){
                getController().signOut();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(context instanceof ListNotesFragment.Controller)) {
            throw new IllegalStateException("Activity must implement Controller");
        }
    }

    private SettingsFragment.Controller getController() {
        return (SettingsFragment.Controller) getActivity();
    }

    public interface Controller {
        void deleteAll();
        void signIn();
        void signOut();
        String getUserName();
    }
}
