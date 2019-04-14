package com.android.tony.surveyx;

import android.content.DialogInterface;
import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText nameEditText,emailEditText,passwordEditText;
    Button signUpButton;
    ProgressBar sigUpProgressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        nameEditText = findViewById(R.id.signupnameeditText);
        emailEditText = findViewById(R.id.signupemailedittext);
        passwordEditText = findViewById(R.id.signuppasswordeditText);
        sigUpProgressbar = findViewById(R.id.signupprogressBar);
        signUpButton = findViewById(R.id.signupbutton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sigUpProgressbar.setVisibility(View.VISIBLE);
                if( nameEditText.getText().toString().trim().isEmpty() ||emailEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All firelds r required",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.createUserWithEmailAndPassword(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                        firebaseAuth.signOut();
                                                    else
                                                        Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            new AlertDialog.Builder(SignUpActivity.this).setIcon(R.mipmap.surveyx).setTitle("Verify Email").setMessage("A email verification link is sent please verify to continue...").setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    SignUpActivity.this.finish();
                                                    sigUpProgressbar.setVisibility(View.GONE);
                                                    dialog.cancel();
                                                }
                                            }).create().show();

                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                            sigUpProgressbar.setVisibility(View.GONE);
                                        }
                                    }
                                });

                            }
                            else {
                                Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                sigUpProgressbar.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            }
        });
    }
}
