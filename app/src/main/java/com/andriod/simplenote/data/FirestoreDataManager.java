package com.andriod.simplenote.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.andriod.simplenote.entity.Note;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashSet;
import java.util.Set;

public class FirestoreDataManager extends BaseDataManager {
    private static final String TAG = "@@@FirestoreDataManager";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Set<Note> notes = new HashSet<>();
    private String collection = "notes";

    public FirestoreDataManager() {
        db.collection(collection)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Note note = doc.toObject(Note.class);
                        note.setId(doc.getId());
                        notes.add(note);
                    }
                    notifySubscribers();
                });

    }

    @Override
    public Set<Note> getData() {
        return notes;
    }

    @Override
    public void updateData(Note note) {
        String id = note.getId();

        if (id == null || id.isEmpty()) {
            db.collection(collection)
                    .add(note)
                    .addOnSuccessListener(documentReference -> {
                        note.setId(documentReference.getId());
                        Log.i(TAG, String.format("Note#%s was added", note.getId()));
                        notes.add(note);
                        notifySubscribers();
                    });
        } else {
            db.collection(collection)
                    .document(id)
                    .set(note)
                    .addOnSuccessListener(unused -> {
                        Log.i(TAG, String.format("Note#%s was changed", note.getId()));
                        notifySubscribers();
                    });
        }
    }

    @Override
    public void deleteData(Note note) {
        db.collection(collection)
                .document(note.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, String.format("Note#%s was deleted", note.getId()));
                    notes.remove(note);
                    notifySubscribers();
                });
    }

    @Override
    public void deleteAll() {
        for (Note note : notes) {
            deleteData(note);
        }
    }
}
