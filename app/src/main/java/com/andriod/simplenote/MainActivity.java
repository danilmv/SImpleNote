package com.andriod.simplenote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.andriod.simplenote.entity.Note;
import com.andriod.simplenote.fragments.ListNotesFragment;
import com.andriod.simplenote.fragments.NoteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements ListNotesFragment.Controller,
        NoteFragment.Controller {

    private static final String FRAGMENT_LIST_NOTES = "FRAGMENT_LIST_NOTES";
    private static final String FRAGMENT_NOTE = "FRAGMENT_NOTE";
    private static final String TAG = "@@@MainActivity@";
    private static final String FRAGMENT_LIST_NOTES_FAVORITES = "FRAGMENT_LIST_NOTES_FAVORITES";

    private boolean hasSecondContainer;
    private BottomNavigationView bottomNavigationView;
    private String currentListTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasSecondContainer = findViewById(R.id.second_fragment_container) != null;
        bottomNavigationView = findViewById(R.id.bottom_view);

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

    private void showList(boolean showOnlyFavorites) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ListNotesFragment.SHOW_FAVORITES_KEY, showOnlyFavorites);

        String tag = showOnlyFavorites ? FRAGMENT_LIST_NOTES_FAVORITES : FRAGMENT_LIST_NOTES;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.main_fragment_container, ListNotesFragment.class, bundle, tag);
        if (currentListTag != null && !currentListTag.equals(tag)) {
            transaction.addToBackStack(currentListTag);
        }
        transaction.commit();

        currentListTag = tag;
    }

    private void showNote(Note note) {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(hasSecondContainer ? R.id.second_fragment_container : R.id.main_fragment_container, NoteFragment.newInstance(note), FRAGMENT_NOTE);
        if (!hasSecondContainer) fragmentTransaction.addToBackStack(currentListTag);
        fragmentTransaction.commit();
    }

    private void showSettings() {
    }


    @Override
    public void changeNote(Note note) {
        showNote(note);
    }

    @Override
    public void noteSaved(Note note) {
        ListNotesFragment listNotesFragment = (ListNotesFragment) getSupportFragmentManager().findFragmentByTag(currentListTag);

        if (listNotesFragment != null) {
            getSupportFragmentManager().popBackStack();
            listNotesFragment.addNote(note);
        }
    }
}