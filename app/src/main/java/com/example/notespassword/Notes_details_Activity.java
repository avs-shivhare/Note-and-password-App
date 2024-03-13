package com.example.notespassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class Notes_details_Activity extends AppCompatActivity {

    private TextView title,content;
    FloatingActionButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        Toolbar toolbar = findViewById(R.id.toolbar_details_notes);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        button = findViewById(R.id.floating_button_details_notes);
        title = findViewById(R.id.title_details_notes);
        content = findViewById(R.id.content_details_notes);
        title.setText(data.getStringExtra("title"));
        content.setText(data.getStringExtra("content"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Edit_notes_Activity.class);
                i.putExtra("title",data.getStringExtra("title"));
                i.putExtra("content",data.getStringExtra("content"));
                i.putExtra("noteId",data.getStringExtra("noteId"));
                startActivity(i);
            }
        });

    }
}