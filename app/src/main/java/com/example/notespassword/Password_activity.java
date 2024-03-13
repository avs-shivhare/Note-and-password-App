package com.example.notespassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.security.SecureRandom;

public class Password_activity extends AppCompatActivity {

    Button button;
    EditText phint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        getSupportActionBar().setTitle("Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = findViewById(R.id.btn_generate_password);
        phint = findViewById(R.id.txt_hint_password);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ghint = phint.getText().toString();
                if(ghint.length() > 8) {
                    phint.setError("Please enter hint that length up to  8 character");
                    phint.requestFocus();
                } else {
                    String password = generateStrongPassword(ghint);
                    Intent i = new Intent(Password_activity.this,Password_edit_Activity.class);
                    i.putExtra("passwordId",password);
                    startActivity(i);

                }
            }
        });
    }
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_+-=[]{}|;:'\",.<>?";
    public String generateStrongPassword(String hint) {
        String characters = UPPER + LOWER + DIGITS + SPECIAL;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        int length = 12; // Change this to the desired password length

        if (hint != null && !hint.isEmpty()) {
            password.append(hint);
            length -= hint.length();
        }

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }
}