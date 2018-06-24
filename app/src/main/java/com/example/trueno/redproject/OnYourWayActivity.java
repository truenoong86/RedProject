package com.example.trueno.redproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class OnYourWayActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_your_way);

        close = (android.support.v7.widget.Toolbar) findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnYourWayActivity.super.onBackPressed();
            }
        });
    }
}
