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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    TextView signUpLogin,forgetPasswordLogin;
    Button loginLogin;
    EditText emailLogin, passwordLogin;
    private FirebaseAuth auth;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signUpLogin = findViewById(R.id.txt_for_signup_login);
        loginLogin = findViewById(R.id.btn_login_login);
        emailLogin = findViewById(R.id.txt_email_login);
        passwordLogin = findViewById(R.id.txt_password_login);
        forgetPasswordLogin= findViewById(R.id.forget_password_login);
        progressBarLogin = findViewById(R.id.visibility_login);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user != null) {
            finish();
            startActivity(new Intent(Login_Activity.this, Choose_Activity.class));

        }
        signUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIn = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(loginIn);
            }
        });

        forgetPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this, Forget_password_Activity.class));
            }
        });
        loginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();
                if(email.isEmpty() && password.isEmpty()) {
                    emailLogin.setError("Please enter email");
                    emailLogin.requestFocus();
                    passwordLogin.setError("please enter password");
                    passwordLogin.requestFocus();
                }
                else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailLogin.setError("Please enter valid email id");
                    emailLogin.requestFocus();
                }
                else if (password.isEmpty()) {
                    passwordLogin.setError("Please enter password");
                    passwordLogin.requestFocus();
                }
                else if(password.length() < 8) {
                    passwordLogin.setError("Password minimum length 8");
                    passwordLogin.requestFocus();
                }
                else {
                    progressBarLogin.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                checkEmailVerification();
                            }
                            else {
                                Toast.makeText(Login_Activity.this, "User is not registered", Toast.LENGTH_SHORT).show();
                                progressBarLogin.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }
    private void checkEmailVerification() {
        FirebaseUser user = auth.getCurrentUser();
        if(user.isEmailVerified() == true) {
            progressBarLogin.setVisibility(View.GONE);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(Login_Activity.this, Choose_Activity.class));
        }
        else {
            progressBarLogin.setVisibility(View.GONE);
            Toast.makeText(this, "Verify email first", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }
    }
}