package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editEmail, editPassword;

    private EditText signInEmail;
    private EditText signInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();



        //signed in
        //signed out
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //signed in
                } else {
                    //signed out
                }
            }

        };
        signInEmail = (EditText) findViewById(R.id.email_sign_in);
        signInPassword = (EditText) findViewById(R.id.pswd_sign_in);

        Button signInBtn = (Button) findViewById(R.id.sign_in);
        signInBtn.setOnClickListener(this);

        TextView signInBack = (TextView) findViewById(R.id.sign_in_back);
        signInBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                signIn(signInEmail.getText().toString(), signInPassword.getText().toString());
                break;
            case R.id.sign_in_back:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    public void signIn(String email, String password) {

        String emailInput = editEmail.getText().toString().trim();
        String passwordInput = editPassword.getText().toString().trim();

        if(emailInput.isEmpty()) {
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }

        if(passwordInput.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Signed in successfully! ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignIn.this, "Signing in is failed! ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}