package com.andriod.simplenote.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.andriod.simplenote.R;
import com.andriod.simplenote.entity.Note;

import java.util.ArrayList;
import java.util.List;

public class ListNotesFragment extends Fragment {

    private LinearLayout container;
    private final List<Note> notes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container = view.findViewById(R.id.list_container);

        view.findViewById(R.id.button_add_note).setOnClickListener(v -> {
            if (getController() != null) {
                getController().changeNote(new Note());
            }
        });

        showList();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof Controller)){
            throw new IllegalStateException("Activity must implement Controller");
        }
    }

    private Controller getController() {
        return (Controller) getActivity();
    }

    public interface Controller {
        void changeNote(Note note);
    }

    private void showList(){
        container.removeAllViews();
        for (Note note : notes) {
            TextView itemView = new TextView(getContext());
            itemView.setText(note.getHeader());
            container.addView(itemView);
        }
    }
}
