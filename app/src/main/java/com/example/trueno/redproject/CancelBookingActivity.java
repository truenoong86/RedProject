package com.example.trueno.redproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CancelBookingActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);

        close = (android.support.v7.widget.Toolbar) findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelBookingActivity.super.onBackPressed();
            }
        });
    }
}
