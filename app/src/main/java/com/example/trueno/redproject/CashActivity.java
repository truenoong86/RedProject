package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CashActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar back;

    LinearLayout cardLayout, cashLayout;
    ImageView cardCheck, cashCheck;
    String paymentPref = "Cash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //startActivity(new Intent(CashActivity.this, MainActivity.class));
            Intent intent = new Intent();
            intent.putExtra("paymentPref", paymentPref);
            setResult(RESULT_OK, intent);
            finish();
            }
        });

        cardLayout = findViewById(R.id.layoutCard);
        cashLayout = findViewById(R.id.layoutCash);
        cardCheck = findViewById(R.id.cardCheck);
        cashCheck = findViewById(R.id.cashCheck);


        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child("passenger").child(passengerId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("cardDetails")){
                    cardLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Intent intent = getIntent();

        if(intent != null){
            String intentPref = getIntent().getStringExtra("paymentPref");
            paymentPref = intentPref;
            if(intentPref.equalsIgnoreCase("Cash")){
                cardCheck.setVisibility(View.INVISIBLE);
                cashCheck.setVisibility(View.VISIBLE);


            } else if (intentPref.equalsIgnoreCase("Card")){
                cashCheck.setVisibility(View.INVISIBLE);
                cardCheck.setVisibility(View.VISIBLE);

            }
        }


        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCheck.setVisibility(View.VISIBLE);
                cashCheck.setVisibility(View.INVISIBLE);

                paymentPref = "Card";

            }
        });

        cashLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardCheck.setVisibility(View.INVISIBLE);
                cashCheck.setVisibility(View.VISIBLE);

                paymentPref = "Cash";


            }
        });
    }
}
