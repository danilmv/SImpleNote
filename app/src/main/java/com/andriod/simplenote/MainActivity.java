package com.andriod.simplenote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.andriod.simplenote.data.BaseDataManager;
import com.andriod.simplenote.entity.Note;
import com.andriod.simplenote.fragments.ListNotesFragment;
import com.andriod.simplenote.fragments.NoteFragment;
import com.andriod.simplenote.fragments.SettingsFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements ListNotesFragment.Controller,
        NoteFragment.Controller,
        SettingsFragment.Controller {

    private static final String FRAGMENT_LIST_NOTES = "FRAGMENT_LIST_NOTES";
    private static final String FRAGMENT_NOTE = "FRAGMENT_NOTE";
    private static final String FRAGMENT_SETTINGS = "FRAGMENT_SETTINGS";
    private static final String TAG = "@@@MainActivity@";
    private static final int CODE_SIGN_IN = 1111;

    private boolean hasSecondContainer;
    private BottomNavigationView bottomNavigationView;

    private GoogleSignInClient googleSignInClient;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
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
    }

    private void showList(boolean showOnlyFavorites) {
        if (userName == null || userName.isEmpty()) {
            signIn();
            return;
        }

        Log.d(TAG, "showList() called with: showOnlyFavorites = [" + showOnlyFavorites + "]");
        ListNotesFragment fragment = (ListNotesFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_LIST_NOTES);
        if (fragment == null) {
            fragment = new ListNotesFragment();
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, FRAGMENT_LIST_NOTES)
                .commit();

        fragment.setMode(showOnlyFavorites);

        setBottomView(showOnlyFavorites ? R.id.menu_bottom_item_favorites : R.id.menu_bottom_item_list);
    }

    private void showNote(Note note) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        hasSecondContainer ? R.id.second_fragment_container : R.id.main_fragment_container,
                        NoteFragment.newInstance(note), FRAGMENT_NOTE)
                .commit();
    }

    private void showSettings() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        hasSecondContainer ? R.id.second_fragment_container : R.id.main_fragment_container,
                        new SettingsFragment(), FRAGMENT_SETTINGS)
                .commit();
    }

    @Override
    public void changeNote(Note note) {
        showNote(note);
    }

    @Override
    public void noteSaved(Note note) {
        getDataManager().updateData(note);
        showList(false);
    }

    private BaseDataManager getDataManager() {
        return ((NoteApplication) getApplication()).getDataManager();
    }

    @Override
    public void deleteAll() {
        getDataManager().deleteAll();
        showList(false);
    }

    private void setBottomView(int bottomItemId) {
        Log.d(TAG, "setBottomView() called with: bottomItemId = [" + bottomItemId + "]");
        if (bottomNavigationView != null) {
            MenuItem item = bottomNavigationView.getMenu().findItem(bottomItemId);
            if (item != null) item.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume() called");
        super.onResume();

        setBottomView(R.id.menu_bottom_item_list);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart() called");
        super.onStart();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Account account = gso.getAccount();
        if (account == null) {
            GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (googleAccount != null) {
                account = googleAccount.getAccount();
            }
        }

        if (account != null) {
            userName = account.name;
        }

        showList(false);
    }

    @Override
    public void signIn() {
        startActivityForResult(googleSignInClient.getSignInIntent(), CODE_SIGN_IN);
    }

    @Override
    public void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(task -> showSettings());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null && account.getAccount() != null) {
                    userName = account.getAccount().name;
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }

            if (userName != null && !userName.isEmpty()) {
                getDataManager().setUser(userName);

                showList(false);
            }
        }
    }

    @Override
    public String getUserName() {
        return userName;
    }
}
