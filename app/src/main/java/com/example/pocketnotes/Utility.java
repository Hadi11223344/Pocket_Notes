package com.example.pocketnotes;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
     static void Toast(Context context, String message ) {
         Toast.makeText(context, message, LENGTH_SHORT).show();
        }
        static CollectionReference getCollectionReferenceForNotes(){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
             return FirebaseFirestore.getInstance().collection("notes")
                             .document(currentUser.getUid()).collection("my_notes");

         }
     static String timestampToString(Timestamp t){
        return new SimpleDateFormat("MM/dd/yy").format(t.toDate());
     }

    }

