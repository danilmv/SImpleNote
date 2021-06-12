package com.andriod.simplenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.andriod.simplenote.entity.Note;
import com.andriod.simplenote.fragments.ListNotesFragment;
import com.andriod.simplenote.fragments.NoteFragment;
import com.andriod.simplenote.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements ListNotesFragment.Controller,
        NoteFragment.Controller,
        SettingsFragment.Controller {

    private static final String FRAGMENT_LIST_NOTES = "FRAGMENT_LIST_NOTES";
    private static final String FRAGMENT_NOTE = "FRAGMENT_NOTE";
    private static final String FRAGMENT_SETTINGS = "FRAGMENT_SETTINGS";
    private static final String TAG = "@@@MainActivity@";

    private boolean hasSecondContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasSecondContainer = findViewById(R.id.second_fragment_container) != null;
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            Log.d(TAG, String.format("setOnNavigationItemSelectedListener() called: %s", item.getTitle()));

            if (itemId == R.id.menu_bottom_item_list) {
                showList(false);
            } else if (itemId == R.id.menu_bottom_item_favorites) {
                showList(true);
            } else if (itemId == R.id.menu_bottom_item_settings) {
                showSettings();
            } else {
                return false;
            }
            return true;
        });

        showList(false);
    }

    private ListNotesFragment showList() {
        Log.d(TAG, "showList() called");

        ListNotesFragment fragment = (ListNotesFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_LIST_NOTES);
        if (fragment == null) {
            fragment = new ListNotesFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, FRAGMENT_LIST_NOTES)
                .commit();

        return fragment;
    }

    private ListNotesFragment showList(boolean showOnlyFavorites) {
        Log.d(TAG, "showList() called with: showOnlyFavorites = [" + showOnlyFavorites + "]");
        ListNotesFragment fragment = showList();
        fragment.setMode(showOnlyFavorites);
        return fragment;
    }

    private void showNote(Note note) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (hasSecondContainer) {
            transaction.replace(R.id.second_fragment_container, NoteFragment.newInstance(note), FRAGMENT_NOTE);
        } else {
            transaction.add(R.id.main_fragment_container, NoteFragment.newInstance(note), FRAGMENT_NOTE);
        }
        transaction.commit();
    }

    private void showSettings() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (hasSecondContainer) {
            transaction.replace(R.id.second_fragment_container, new SettingsFragment(), FRAGMENT_SETTINGS);
        } else {
            transaction.add(R.id.main_fragment_container, new SettingsFragment(), FRAGMENT_SETTINGS);
        }
        transaction.commit();
    }

    @Override
    public void changeNote(Note note) {
        showNote(note);
    }

    @Override
    public void noteSaved(Note note) {
        showList().addNote(note);
    }

    @Override
    public void deleteAll() {
        showList(false).deleteAll();
    }
}