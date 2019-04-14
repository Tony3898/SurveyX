package com.android.tony.surveyx;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
// code by https://linkedin.com/in/tejas-rana-668595128/
// Tony Rana
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText emailEditText,passwordEditText;
    TextView signinuptextView;
    Button signInButton;
    ProgressBar signProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        firebaseAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.signinemaileditText);
        passwordEditText= findViewById(R.id.siginpasswordeditText);
        signProgressbar = findViewById(R.id.signinprogressBar);

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified())
        {
            finish();
            startActivity(new Intent(SignInActivity.this,SurveyListActivity.class));
        }


        signinuptextView = findViewById(R.id.signinuptextview);
        signinuptextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });

        signInButton = findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signProgressbar.setVisibility(View.VISIBLE);
                if(emailEditText.getText().toString().trim().isEmpty() || passwordEditText.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All firelds r required",Toast.LENGTH_SHORT).show();
                    signProgressbar.setVisibility(View.GONE);
                }
                else
                {
                    firebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful() )
                            {
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    signProgressbar.setVisibility(View.GONE);
                                    startActivity(new Intent(SignInActivity.this,SurveyListActivity.class));
                                    finish();
                                }
                                else
                                {
                                    new AlertDialog.Builder(SignInActivity.this).setIcon(R.mipmap.ic_launcher).setTitle("Verify Email").setMessage("A email verification link is sent please verify to continue...").setCancelable(false).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            firebaseAuth.signOut();
                                            signProgressbar.setVisibility(View.GONE);
                                            dialog.cancel();
                                        }
                                    }).setNeutralButton("Send Email Verifiacation Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            signProgressbar.setVisibility(View.GONE);
                                            firebaseAuth.getCurrentUser().sendEmailVerification();
                                            firebaseAuth.signOut();
                                            dialog.cancel();
                                        }
                                    }).create().show();
                                }

                            }
                            else {
                                Toast.makeText(SignInActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                signProgressbar.setVisibility(View.GONE);
                            }
                        }
                    });
                }

            }
        });
    }
}
