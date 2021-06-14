package com.andriod.simplenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.andriod.simplenote.entity.Note;
import com.andriod.simplenote.fragments.ListNotesFragment;
import com.andriod.simplenote.fragments.NoteFragment;

public class MainActivity extends AppCompatActivity
        implements ListNotesFragment.Controller,
        NoteFragment.Controller {

    private static final String FRAGMENT_LIST_NOTES = "FRAGMENT_LIST_NOTES";
    private static final String FRAGMENT_NOTE = "FRAGMENT_NOTE";

    private boolean hasSecondContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasSecondContainer = findViewById(R.id.second_fragment_container) != null;

        showList();
    }

    private void showList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new ListNotesFragment(), FRAGMENT_LIST_NOTES)
                .commit();
    }

    private void showNote(Note note) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(hasSecondContainer ? R.id.second_fragment_container : R.id.main_fragment_container, NoteFragment.newInstance(note), FRAGMENT_NOTE);
        if (!hasSecondContainer) fragmentTransaction.addToBackStack(FRAGMENT_LIST_NOTES);
        fragmentTransaction.commit();
    }

    @Override
    public void changeNote(Note note) {
        showNote(note);
    }

    @Override
    public void noteSaved(Note note) {
        ListNotesFragment listNotesFragment = (ListNotesFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_NOTES);

        if (listNotesFragment != null) {
            getSupportFragmentManager().popBackStack();
            listNotesFragment.addNote(note);
        }
    }
}