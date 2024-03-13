package com.example.notespassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forget_password_Activity extends AppCompatActivity {
    private EditText emailForgetPassword;
    private Button forgetPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBarForget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setTitle("Forget Password");

        emailForgetPassword = findViewById(R.id.txt_email_forget_password);
        forgetPassword = findViewById(R.id.btn_forget_password);
        auth = FirebaseAuth.getInstance();
        progressBarForget = findViewById(R.id.visibility_forget_password);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailForgetPassword.getText().toString();
                if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailForgetPassword.setError("Enter correct email id");
                    emailForgetPassword.requestFocus();
                }
                else {
                    progressBarForget.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                progressBarForget.setVisibility(View.GONE);
                                Toast.makeText(Forget_password_Activity.this, "Password forget mail send successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Forget_password_Activity.this, Login_Activity.class));
                            }
                            else {
                                Toast.makeText(Forget_password_Activity.this, "User doesn't exists", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}