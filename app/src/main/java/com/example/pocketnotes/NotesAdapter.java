package com.example.pocketnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentId;

public class NotesAdapter extends FirestoreRecyclerAdapter<Note , NotesAdapter.NoteViewHolder> {
Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    // this binds our data with our view while binding
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {

        holder.tvTitle.setText(note.getTitle());
        holder.tvTimestamp.setText(Utility.timestampToString(note.getTimestamp()));
        holder.tvContent.setText(note.getContent());
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NotesDetail.class);
                intent.putExtra("title",note.getTitle());
                intent.putExtra("content",note.getContent());
                String docID = getSnapshots().getSnapshot(position).getId();
                intent.putExtra("DocID", docID);
                context.startActivity(intent);
            }
        });


    }

    // it holds the individual note view so it is called noteViewHolder
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note_design,parent,false);
        return new NoteViewHolder(view);

    }

     class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle,tvContent,tvTimestamp;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNoteTitle);
            tvContent = itemView.findViewById(R.id.tvNoteContent);
            tvTimestamp = itemView.findViewById(R.id.tvNoteTimestamp);
        }
    }

}
