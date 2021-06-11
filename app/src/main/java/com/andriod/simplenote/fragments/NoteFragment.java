package com.andriod.simplenote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.simplenote.R;
import com.andriod.simplenote.entity.Note;

public class NoteFragment extends Fragment {

    private static final String NOTE_EXTRA_KEY = "NOTE_EXTRA_KEY";

    private Note note;

    private EditText editTextHeader;

    public static NoteFragment newInstance(Note note) {
        NoteFragment instance = new NoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_EXTRA_KEY, note);
        instance.setArguments(bundle);

        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            note = getArguments().getParcelable(NOTE_EXTRA_KEY);
        }

        editTextHeader = view.findViewById(R.id.edit_text_header);
        editTextHeader.setText(note.getHeader());
        ToggleButton toggleButtonFavorite = view.findViewById(R.id.toggle_favorite);
        toggleButtonFavorite.setChecked(note.isFavorite());

        view.findViewById(R.id.button_save_note).setOnClickListener(v -> {
            if (getController() != null) {
                note.setHeader(editTextHeader.getText().toString());
                note.setFavorite(toggleButtonFavorite.isChecked());
                getController().noteSaved(note);
            }
        });
    }

    private Controller getController() {
        return (Controller) getActivity();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (!(context instanceof ListNotesFragment.Controller)) {
            throw new IllegalStateException("Activity must implement Controller");
        }
    }

    public interface Controller {
        void noteSaved(Note note);
    }
}
