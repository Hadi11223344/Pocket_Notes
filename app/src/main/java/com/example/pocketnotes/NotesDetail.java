package com.example.pocketnotes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetail extends AppCompatActivity {
            ImageView ivAddNote;
            EditText title, content;
            TextView etDeleteNote, etPageTitle;
            String strTitle, strContent, docID;
            boolean isEditMode = false;


            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                EdgeToEdge.enable(this);
                setContentView(R.layout.activity_notes_detail);
                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });

                init();

                strTitle = getIntent().getStringExtra("title");
                strContent = getIntent().getStringExtra("content");
                docID = getIntent().getStringExtra("docID");


                if (docID != null && !docID.isEmpty()) {
                    isEditMode = true;
                }
                title.setText(strTitle);
                content.setText(strContent);
                if (isEditMode) {
                    etPageTitle.setText("Edit your note");
                    etDeleteNote.setVisibility(View.VISIBLE);

                }


                ivAddNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveNote();
                    }
                });

                etDeleteNote.setOnClickListener((v) -> deleteNoteFromFirebase());
            }

            private void saveNote() {
                String noteTitle = title.getText().toString().trim();
                String noteContent = content.getText().toString().trim();
                if ((noteTitle == null) || noteTitle.isEmpty()) {
                    this.title.setError("Title is required");
                    return;
                }

                Note note = new Note();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setTimestamp(Timestamp.now());

                saveNoteToFirebase(note);
            }

            private void saveNoteToFirebase(Note note) {
                DocumentReference documentReference;
                if (isEditMode) {
                    // this will update existing note
                    documentReference = Utility.getCollectionReferenceForNotes().document(docID);

                } else {
                    // this will create new node
                    documentReference = Utility.getCollectionReferenceForNotes().document();
                }
                documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utility.Toast(NotesDetail.this, "Note added successfully");
                            finish();
                        } else {
                            Utility.Toast(NotesDetail.this, "Failed while adding note");
                        }
                    }
                });
            }

                    void deleteNoteFromFirebase() {
                        DocumentReference documentReference;

                            // this will update existing note
                            documentReference = Utility.getCollectionReferenceForNotes().document(docID);


                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Utility.Toast(NotesDetail.this, "Note deleted successfully");
                                        finish();
                                    } else {
                                        Utility.Toast(NotesDetail.this, "Failed while deleting note");
                                    }
                                }
                            });



                    }

                    private void init (){
                        ivAddNote = findViewById(R.id.save_note_btn);
                        title = findViewById(R.id.notes_title_text);
                        content = findViewById(R.id.notes_content_text);
                        etDeleteNote = findViewById(R.id.delete_note_text_view_btn);
                        etPageTitle = findViewById(R.id.page_title);

                    }

            }
