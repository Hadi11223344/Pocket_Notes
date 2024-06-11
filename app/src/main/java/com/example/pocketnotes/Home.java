package com.example.pocketnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class Home extends AppCompatActivity {

    FloatingActionButton fabAddNotes;
    RecyclerView rvNotes;
    ImageView btnMenu;
    NotesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
            init();
        fabAddNotes.setOnClickListener(v -> {startActivity(new Intent(Home.this,NotesDetail.class));});
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        setupRecyclerView();
    }
    private void showMenu(){
        // show menu logic
    }
    private void setupRecyclerView(){

        // first thing we do here is query
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query , Note.class).build();

        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter  = new NotesAdapter(options,this);
        rvNotes.setAdapter(adapter);
    }

    private void init(){
        fabAddNotes = findViewById(R.id.fabAdd);
        rvNotes = findViewById(R.id.rvNotes);
        btnMenu = findViewById(R.id.ibMenu);


    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter.notifyDataSetChanged();
    }
}