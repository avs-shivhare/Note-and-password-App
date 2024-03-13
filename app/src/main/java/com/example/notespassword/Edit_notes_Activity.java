package com.example.notespassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edit_notes_Activity extends AppCompatActivity {
    EditText title,content;
    FloatingActionButton button;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = findViewById(R.id.toolbar_edit_notes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.title_edit_notes);
        content = findViewById(R.id.content_edit_notes);
        button = findViewById(R.id.floating_button_edit_notes);
        progressBar = findViewById(R.id.progress_edit_notes);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Intent data = getIntent();
        String mtitle = data.getStringExtra("title");
        String mcontent = data.getStringExtra("content");
        title.setText(mtitle);
        content.setText(mcontent);
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Edit_notes_Activity.this, "Button is clicked", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.VISIBLE);
                String newtitle = title.getText().toString();
                String newcontent = content.getText().toString();
                if(newtitle.isEmpty() || newcontent.isEmpty()) {
                    Toast.makeText(Edit_notes_Activity.this, "fill title or content first", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    //DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(Objects.requireNonNull(data.getStringExtra("noteId")));

                    Map<String,Object> notes = new HashMap<>();
                    notes.put("title",newtitle);
                    notes.put("content",newcontent);
                    firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").whereEqualTo("title",mtitle).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentId = documentSnapshot.getId();
                                firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(documentId).update(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Edit_notes_Activity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Edit_notes_Activity.this, Notes_Activity.class));
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Edit_notes_Activity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(Edit_notes_Activity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    documentReference.set(notes).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void unused) {
//                            Toast.makeText(Edit_notes_Activity.this, "note update successfully", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(Edit_notes_Activity.this,Notes_Activity.class));
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Edit_notes_Activity.this, "Failed to update", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    });

                }
            }
        });
        
        
    }
}