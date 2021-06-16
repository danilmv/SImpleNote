package com.andriod.simplenote.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.andriod.simplenote.R;
import com.andriod.simplenote.adapter.ListNotesAdapter;
import com.andriod.simplenote.entity.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListNotesFragment extends Fragment {

    private static final String TAG = "@@@ListNotesFragment@";
    private static final String SHARED_PREFERENCES_NOTES = "SHARED_PREFERENCES_NOTES";
    private static final String LIST_NOTES_KEY = "LIST_NOTES_KEY";
    private final Set<Note> notes = new HashSet<>();
    private final Gson gson = new Gson();

    private boolean showOnlyFavorites;

    private ListNotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView() called");
        return inflater.inflate(R.layout.fragment_list_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, String.format("onViewCreated() called with: notes.size = [%d]", notes.size()));

        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        adapter = new ListNotesAdapter();
        adapter.setOnItemClickListener(note -> {
            if (getController() != null) {
                getController().changeNote(note);
            }
        });
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.button_add_note).setOnClickListener(this::showPopupMenu);

        showList();
    }

    private void showPopupMenu(View v) {
        if (getController() != null) {
            PopupMenu popupMenu = new PopupMenu(getContext(), v);
            Menu menu = popupMenu.getMenu();
            for (Note.NoteType value : Note.NoteType.values()) {
                MenuItem item = menu.add(value.name());
                item.setOnMenuItemClickListener(item1 -> {
                    getController().changeNote(new Note(value));
                    return true;
                });
            }
            popupMenu.show();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.d(TAG, "onAttach() called");
        super.onAttach(context);
        if (!(context instanceof Controller)) {
            throw new IllegalStateException("Activity must implement Controller");
        }

        loadData(context);
    }

    private void loadData(Context context) {
        Log.d(TAG, "loadData() called with: context = [" + context + "]");
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NOTES, Context.MODE_PRIVATE);
        String stringData = sharedPreferences.getString(LIST_NOTES_KEY, null);
        if (stringData != null && !stringData.isEmpty()) {
            Type setType = new TypeToken<HashSet<Note>>() {
            }.getType();
            notes.addAll(gson.fromJson(stringData, setType));
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
        if (adapter == null) return;

        List<Note> list;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            list = notes.stream()
                    .filter(note -> !showOnlyFavorites || note.isFavorite())
                    .collect(Collectors.toList());
        } else {
            list = new ArrayList<>(notes);
        }
        adapter.setData(list);
    }

    public void addNote(Note note) {
        notes.add(note);
        showList();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    private void saveData() {
        Log.d(TAG, "saveData: ");
        if (getActivity() != null) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCES_NOTES, Context.MODE_PRIVATE);
            sharedPreferences
                    .edit()
                    .putString(LIST_NOTES_KEY, gson.toJson(notes))
                    .apply();
        }
    }

    public void setMode(boolean showOnlyFavorites) {
        this.showOnlyFavorites = showOnlyFavorites;
        showList();
    }

    public void deleteAll() {
        Log.d(TAG, "deleteAll() called");
        notes.clear();
        saveData();
        showList();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume() called");
        super.onResume();
    }
}
