package com.example.notespassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Choose_Activity extends AppCompatActivity {

    private Button password, notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        getSupportActionBar().setTitle("Welcome");
        password = findViewById(R.id.btn_password);
        notes = findViewById(R.id.btn_notes);

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choose_Activity.this, Password_activity.class));
            }
        });

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Choose_Activity.this, Notes_Activity.class));
            }
        });
    }
}