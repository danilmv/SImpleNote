package com.andriod.simplenote.data;

import android.content.SharedPreferences;

import com.andriod.simplenote.entity.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PreferencesDataManagerImpl extends BaseDataManager {
    private static final String LIST_NOTES_KEY = "LIST_NOTES_KEY";

    private final Set<Note> notes = new HashSet<>();
    private final Gson gson = new Gson();
    private SharedPreferences sharedPreferences;

    public PreferencesDataManagerImpl(SharedPreferences preferences) {
        setSharedPreferences(preferences);

        String stringData = sharedPreferences.getString(LIST_NOTES_KEY, null);
        if (stringData != null && !stringData.isEmpty()) {
            Type setType = new TypeToken<HashSet<Note>>() {
            }.getType();
            notes.addAll(gson.fromJson(stringData, setType));
        }
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Set<Note> getData() {
        return notes;
    }

    @Override
    public void updateData(Note note) {
        if (note != null) {
            String id = note.getId();
            if (id == null || id.isEmpty()) note.setId(UUID.randomUUID().toString());

            notes.add(note);
        }
        saveData();
    }

    @Override
    public void deleteData(Note note) {
        notes.remove(note);
        saveData();
    }

    @Override
    public void deleteAll() {
        notes.clear();
        saveData();
    }

    private void saveData() {
        sharedPreferences
                .edit()
                .putString(LIST_NOTES_KEY, gson.toJson(notes))
                .apply();

        notifySubscribers();
    }
}
