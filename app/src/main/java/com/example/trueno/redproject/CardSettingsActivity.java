package com.example.trueno.redproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CardSettingsActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar back;
    Button btnDelete;
    TextView tvLast4, tvExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_settings);

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        btnDelete = findViewById(R.id.btnDelete);
        tvLast4 = findViewById(R.id.tvLast4);
        tvExpiry = findViewById(R.id.tvExpiry);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference passengerRef = database.getReference("/user").child("passenger");
        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cardDetailsRef = passengerRef.child(passengerId);

        cardDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String cardNo = dataSnapshot.child("cardDetails").child("cardNo").getValue().toString();
                    String last4 = cardNo.substring(12);
                    tvLast4.setText("**** **** **** " + last4);

                    String expiryNo = dataSnapshot.child("cardDetails").child("expiryNo").getValue().toString();
                    tvExpiry.setText(expiryNo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            CardSettingsActivity.super.onBackPressed();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference passengerRef = database.getReference("/user").child("passenger");

                String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference cardDetailsRef = passengerRef.child(passengerId);

                cardDetailsRef.child("cardDetails").setValue(null);
                finish();

            }
        });
    }
}
