package com.example.notespassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button loginMain,registerMain;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home");

        loginMain= findViewById(R.id.btn_login_home);
        registerMain = findViewById(R.id.btn_register_home);
        skip= findViewById(R.id.txt_skip_home);

        loginMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loin = new Intent(MainActivity.this, Login_Activity.class);
                startActivity(loin);
            }
        });

        registerMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rein= new Intent(MainActivity.this, SignUp_Activity.class);
                startActivity(rein);
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Skip_password_Activity.class));
            }
        });

    }
}