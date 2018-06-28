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

import com.example.trueno.redproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "SignUp.java";

    private android.support.v7.widget.Toolbar back;
    public TextView tvTerms, tvPrivacyPolicy;
    public EditText editTextUserEmail, editTextPassword, editTextConfirmPassword, etCountryCode, etPhone, etModel, etNumber, etYear, etTyre,etInsurance, etName;
    public Button buttonSignUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.d(TAG, "onCreate: ");
        
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userListRef = firebaseDatabase.getReference("/user/passenger");



        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);
        editTextUserEmail = (EditText) findViewById(R.id.etUserEmail);
        editTextPassword = (EditText) findViewById(R.id.etUserPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.etUserConfirmPassword);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);

        etName = findViewById(R.id.etName);
        etCountryCode = findViewById(R.id.etCountryCode);
        etPhone = findViewById(R.id.etPhoneNumber);
        etModel = findViewById(R.id.etVehicleModel);
        etNumber = findViewById(R.id.etVehicleNumber);
        etYear = findViewById(R.id.etYearOfManufacture);
        etTyre = findViewById(R.id.etTyreSize);
        etInsurance = findViewById(R.id.etInsurance);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.super.onBackPressed();
            }
        });

        tvTerms.setText(Html.fromHtml("<u>Terms of Use</u> "));
        tvPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, TermsActivity.class));
            }
        });

        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, PrivacyPolicyActivity.class));
            }
        });

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

        User user =new User(etName.getText().toString(), etCountryCode.getText().toString() , etPhone.getText().toString(), userEmail, etNumber.getText().toString(), etModel.getText().toString(),etTyre.getText().toString(), etInsurance.getText().toString(), etYear.getText().toString());
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String id = currentUser.getUid();
        userListRef.child(id).setValue(user);

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
