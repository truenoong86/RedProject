package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "SignUp.java";

    private android.support.v7.widget.Toolbar back;
    public TextView tvTerms, tvPrivacyPolicy;
    public EditText editTextUserEmail, editTextPassword, editTextConfirmPassword;
    public Button buttonSignUp;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.d(TAG, "onCreate: ");
        
        firebaseAuth = FirebaseAuth.getInstance();

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);
        editTextUserEmail = (EditText) findViewById(R.id.etUserEmail);
        editTextPassword = (EditText) findViewById(R.id.etUserPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.etUserConfirmPassword);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.super.onBackPressed();
            }
        });

        tvTerms.setText(Html.fromHtml("<u>Terms of Use</u> "));
        tvPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp();
            }
        });
    }

    private void buttonSignUp() {
        String userEmail = editTextUserEmail.getText().toString();
        String userPassword = editTextPassword.getText().toString();
        String userConfirmPassword = editTextConfirmPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Registration fail",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
    }
}
