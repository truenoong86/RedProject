package com.example.trueno.redproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class AddCardActivity extends AppCompatActivity {

    public TextView tvTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        tvTerms = (TextView) findViewById(R.id.tvTerms);

        tvTerms.setText(Html.fromHtml("<u>Terms & Conditions</u> "));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCardActivity.this, TermsActivity.class));
            }
        });
    }
}
