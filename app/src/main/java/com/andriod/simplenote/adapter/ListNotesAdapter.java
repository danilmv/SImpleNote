package com.andriod.simplenote.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andriod.simplenote.R;
import com.andriod.simplenote.entity.Note;

import java.util.List;

public class ListNotesAdapter extends RecyclerView.Adapter<ListNotesAdapter.ViewHolder> {
    private List<Note> notes;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(Note note);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHeader;
        private final ToggleButton toggleFavorite;
        private Note note;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            itemView.setOnClickListener(v -> {
                if (listener!=null){
                    listener.onClick(note);
                }
            });

            textViewHeader = itemView.findViewById(R.id.text_view_header);
            toggleFavorite = itemView.findViewById(R.id.toggle_favorite);
            toggleFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> note.setFavorite(isChecked));
        }

        public void bind(Note note) {
            this.note = note;
            textViewHeader.setText(note.getHeader());
            toggleFavorite.setChecked(note.isFavorite());
        }
    }

    public void setData(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }
}
