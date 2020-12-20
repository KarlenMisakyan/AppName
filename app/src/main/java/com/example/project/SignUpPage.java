package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.FirebaseDatabase;

public class SignUpPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText editFullName, editNickname, editEmail, editPassword;
    private TextView textSignUpBack, labelName;
    private ProgressBar progressBar;
    private Button signUpBtn;

    /**
     *
     * ...
     * final LinearLayout newView = (LinearLayout)getLayoutInflater().inflate(R.layout.single_skill_row, null);
     * myEditText = newView.findViewById(R.id.editDescricao);
     * ...
     * After that in the onClick callback of your button you can get it's text like this:
     *
     * register_btn.setOnClickListener(new View.OnClickListener() {
     *         @Override
     *         public void onClick(View view) {
     *         String text = myEditText.getText().toString();
     * });
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        editFullName = (EditText) findViewById(R.id.full_name_sign_up);
        editNickname = (EditText) findViewById(R.id.nickname_sign_up);
        editEmail = (EditText) findViewById(R.id.email_sign_up);
        editPassword = (EditText) findViewById(R.id.pswd_sign_up);

        signUpBtn = (Button) findViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(this);

        labelName = (TextView) findViewById(R.id.banner_sign_up);
        labelName.setOnClickListener(this);

        textSignUpBack = (TextView) findViewById(R.id.sign_up_back);
        textSignUpBack.setOnClickListener(this);

        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        String fullName = editFullName.getText().toString().trim();
        String nickName = editNickname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        switch(v.getId()) {
            case R.id.sign_up_btn:
                signingUp();
                break;
            case R.id.sign_up_back:
                startActivity(new Intent(this, SignIn.class));
                break;
            case R.id.banner_sign_up:
                startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void signingUp() {

        String fullName = editFullName.getText().toString().trim();
        String nickName = editNickname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();


        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(fullName.isEmpty()) {
            editFullName.setError("Full Name is required!");
            editFullName.requestFocus();
            return;
        }

        if(nickName.isEmpty()) {
            editNickname.setError("Nickname is required!");
            editNickname.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Please, provide valid email");
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        if(password.length() < 8) {
            editPassword.setError("Min password length should be 8 characters");
            editPassword.requestFocus();
            return;
        }

        //progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            User user = new User(fullName, nickName, email, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignUpPage.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);

                                        //redirect to SingIn layout

                                    }else {
                                        Toast.makeText(SignUpPage.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                                        //progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SignUpPage.this, "Failed to register!", Toast.LENGTH_SHORT).show();
                            //progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}