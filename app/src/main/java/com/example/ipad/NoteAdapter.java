package com.example.ipad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Note note);
        void onItemLongClick(Note note);
        void onTodoChecked(Note note, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewContent.setText(currentNote.getContent());
        holder.textViewTime.setText(currentNote.getFormattedTime());
        holder.textViewCategory.setText(currentNote.getCategory());

        // 设置背景色
        if (currentNote.getColor() != 0) {
            holder.cardView.setCardBackgroundColor(currentNote.getColor());
        }

        // 待办事项显示
        if (currentNote.isTodo()) {
            holder.checkBoxTodo.setVisibility(View.VISIBLE);
            holder.checkBoxTodo.setChecked(currentNote.isCompleted());
        } else {
            holder.checkBoxTodo.setVisibility(View.GONE);
        }

        holder.checkBoxTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onTodoChecked(currentNote, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewContent;
        private TextView textViewTime;
        private TextView textViewCategory;
        private com.google.android.material.card.MaterialCardView cardView;
        private CheckBox checkBoxTodo;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            cardView = itemView.findViewById(R.id.cardView);
            checkBoxTodo = itemView.findViewById(R.id.checkBoxTodo);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(notes.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(notes.get(position));
                }
                return true;
            });
        }
    }
}

