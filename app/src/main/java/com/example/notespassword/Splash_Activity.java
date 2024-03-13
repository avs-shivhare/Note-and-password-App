package com.example.notespassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Objects;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Thread s = new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    Thread.sleep(4000);
                }
                catch (Exception e) {
                    Toast.makeText(Splash_Activity.this, "Something is wrong", Toast.LENGTH_SHORT).show();
                }
                finally {
                    Intent i = new Intent(Splash_Activity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        s.start();

    }
}