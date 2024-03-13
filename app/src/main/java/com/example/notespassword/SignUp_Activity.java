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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp_Activity extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;
    TextView loginSignUp;
    Button submitSignUp;
    ProgressBar progressSignUp;
    EditText emailSignUp, passwordSignUp, confirmPassSingUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up");
        auth = FirebaseAuth.getInstance();

        loginSignUp = findViewById(R.id.txt_for_login_signup);
        submitSignUp = findViewById(R.id.btn_submit_signup);
        emailSignUp = findViewById(R.id.txt_email_signup);
        passwordSignUp = findViewById(R.id.txt_password_signup);
        confirmPassSingUp = findViewById(R.id.txt_confirm_signup);
        progressSignUp = findViewById(R.id.visibility_signup);

        loginSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(SignUp_Activity.this, Login_Activity.class);
                startActivity(signIn);
            }
        });

        submitSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailSignUp.getText().toString();
                String password = passwordSignUp.getText().toString();
                String confirm = confirmPassSingUp.getText().toString();
                if(email.isEmpty() && password.isEmpty()) {
                    emailSignUp.setError("Please enter email");
                    emailSignUp.requestFocus();
                    passwordSignUp.setError("please enter password");
                    passwordSignUp.requestFocus();
                }
                else if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailSignUp.setError("Please enter valid email id");
                    emailSignUp.requestFocus();
                }
                else if (password.isEmpty()) {
                    passwordSignUp.setError("Please enter password");
                    passwordSignUp.requestFocus();
                }
                else if(password.length() < 8) {
                    passwordSignUp.setError("Password minimum length 8");
                    passwordSignUp.requestFocus();
                }
                else if(!password.equals(confirm) || confirm.isEmpty()) {
                    passwordSignUp.setError("Password do not match");
                    passwordSignUp.requestFocus();
                    confirmPassSingUp.setError("Password do not match");
                    confirmPassSingUp.requestFocus();
                }
                else {
                    progressSignUp.setVisibility(View.VISIBLE);
                    //FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUp_Activity.this, "Registration is successful", Toast.LENGTH_SHORT).show();
                                fireCall();
                            }
                            else {
                                progressSignUp.setVisibility(View.GONE);
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthUserCollisionException a) {
                                    Toast.makeText(SignUp_Activity.this, "User is already registered", Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e) {
                                    Toast.makeText(SignUp_Activity.this, "Registration is failed please try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });

    }
    private void fireCall() {
        user = auth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(user != null) {
                    Toast.makeText(SignUp_Activity.this, "Registration is successful, verification mail is send to registered mail id kindly verify", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        throw task.getException();
                    }
                    catch (Exception e) {
                        passwordSignUp.setError(""+e);
                        passwordSignUp.requestFocus();
                    }
                }
            }
        });
        auth.signOut();
        Intent signIn = new Intent(SignUp_Activity.this, Login_Activity.class);
        signIn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signIn);
        finish();
    }
}