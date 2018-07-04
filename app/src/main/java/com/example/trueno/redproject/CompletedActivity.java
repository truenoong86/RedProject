package com.example.trueno.redproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

import com.google.firebase.database.FirebaseDatabase;

public class CompletedActivity extends AppCompatActivity {

    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    private void displayCustomerRelqatedObjects() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }
}
