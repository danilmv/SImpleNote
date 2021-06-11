package com.andriod.simplenote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.andriod.simplenote.entity.Note;
import com.andriod.simplenote.fragments.ListNotesFragment;

public class MainActivity extends AppCompatActivity
        implements ListNotesFragment.Controller {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showList();
    }

    private void showList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new ListNotesFragment())
                .commit();
    }

    private void showNote(Note note) {
    }

    @Override
    public void changeNote(Note note) {
        showNote(note);
    }
}