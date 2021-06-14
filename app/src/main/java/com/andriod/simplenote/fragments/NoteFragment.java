package com.andriod.simplenote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.simplenote.R;
import com.andriod.simplenote.entity.Note;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NoteFragment extends Fragment {

    private static final String NOTE_EXTRA_KEY = "NOTE_EXTRA_KEY";

    private Note note;

    private EditText editTextHeader;
    private EditText editTextContent;
    private Spinner spinner;

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
                note.setType(Note.NoteType.valueOf(spinner.getSelectedItem().toString()));
                note.setContent(editTextContent.getText().toString());
                getController().noteSaved(note);
            }
        });

        spinner = view.findViewById(R.id.spinner_note_type);
        List<Note.NoteType> spinnerValues = Arrays.asList(Note.NoteType.values());
        ArrayAdapter<Note.NoteType> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(note.getType()));

        editTextContent = view.findViewById(R.id.edit_text_content);
        editTextContent.setText(note.getContent());
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
