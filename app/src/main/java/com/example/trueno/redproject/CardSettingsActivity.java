package com.example.trueno.redproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CardSettingsActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_settings);

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            CardSettingsActivity.super.onBackPressed();
            }
        });
    }
}
