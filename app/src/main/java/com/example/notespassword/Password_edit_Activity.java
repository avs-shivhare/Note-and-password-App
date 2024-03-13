package com.example.notespassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Password_edit_Activity extends AppCompatActivity {

    EditText notesTitle,notesContent;
    FloatingActionButton save;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore store;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_edit);
        notesTitle = findViewById(R.id.title_edit_password_notes);
        notesContent = findViewById(R.id.content_edit_password_notes);
        save = findViewById(R.id.floating_button_edit_password_notes);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        store = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progress_edit_password_notes);
        Toolbar toolbar = findViewById(R.id.toolbar_edit_password_notes);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        notesContent.setText(i.getStringExtra("passwordId"));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String title = notesTitle.getText().toString();
                String content = notesContent.getText().toString();
                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(Password_edit_Activity.this, "fill title or content first", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    DocumentReference documentReference = store.collection("notes").document(user.getUid()).collection("myNotes").document();
                    Map<String,Object> notes = new HashMap<>();
                    notes.put("title",title);
                    notes.put("content",content);
                    documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Password_edit_Activity.this, "Notes created successfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(Password_edit_Activity.this, Notes_Activity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Password_edit_Activity.this, "Failed to create notes, Please try again", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

    }
}