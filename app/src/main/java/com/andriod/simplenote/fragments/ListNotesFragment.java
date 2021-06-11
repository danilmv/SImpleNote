package com.andriod.simplenote.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashSet;
import java.util.Set;

public class ListNotesFragment extends Fragment {

    private static final String TAG = "@@@ListNotesFragment@";
    private LinearLayout container;
    private final Set<Note> notes = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, String.format("onViewCreated() called with: notes.size = [%d]", notes.size()));

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
        if (!(context instanceof Controller)) {
            throw new IllegalStateException("Activity must implement Controller");
        }
    }

    private Controller getController() {
        return (Controller) getActivity();
    }

    public interface Controller {
        void changeNote(Note note);
    }

    private void showList() {
        Log.d(TAG, String.format("showList() called for note.size = [%d]", notes.size()));

        container.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);

        for (Note note : notes) {
            TextView itemView = new TextView(getContext());
            itemView.setText(note.getHeader());
            itemView.setLayoutParams(params);
            itemView.setTextColor(Color.WHITE);
            itemView.setBackgroundColor(Color.BLUE);
            container.addView(itemView);

            itemView.setOnClickListener(v -> {
                if(getController()!=null)
                    getController().changeNote(note);
            });
        }
    }

    public void addNote(Note note) {
        notes.add(note);
        showList();
    }
}
