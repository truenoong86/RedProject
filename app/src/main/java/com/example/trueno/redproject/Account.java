package com.example.trueno.redproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account extends AppCompatActivity {
    public static final String TAG = "Account.java";

    public TextView textViewName;
    public FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Log.d(TAG, "onCreate: ");

        textViewName = (TextView) findViewById(R.id.textView);
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        String userName = user.getEmail();

        textViewName.setText(userName);
    }
}
