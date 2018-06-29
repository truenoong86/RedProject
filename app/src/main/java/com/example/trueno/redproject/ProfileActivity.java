package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trueno.redproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView tvEditProfile;
    TextView tvTopName,tvName,tvCountryCode, tvPhone, tvEmail,tvVNum,tvVModel,tvYear,tvTyre,tvInsurance;
    private android.support.v7.widget.Toolbar back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvEditProfile = (TextView) findViewById(R.id.tvEditProfile);
        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);

        tvTopName = findViewById(R.id.tvTopName);
        tvName = findViewById(R.id.tvName);
        tvCountryCode = findViewById(R.id.tvCountryCode);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvVNum = findViewById(R.id.tvVehicleNumber);
        tvVModel = findViewById(R.id.tvVehicleModel);
        tvYear = findViewById(R.id.tvYearOfManufacture);
        tvTyre =findViewById(R.id.tvTyreSize);
        tvInsurance = findViewById(R.id.tvInsuranceCompany);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference cardDetailsRef = database.getReference("/user").child("passenger");
        final String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        cardDetailsRef.child(passengerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currUserProfile = dataSnapshot.getValue(com.example.trueno.redproject.models.User.class);
                        tvTopName.setText(currUserProfile.getName());
                        tvName.setText(currUserProfile.getName());
                        tvCountryCode.setText(currUserProfile.getCountryCode());
                        tvPhone.setText(currUserProfile.getPhone());
                        tvEmail.setText(currUserProfile.getEmail());
                        tvVModel.setText(currUserProfile.getVehicleModel());
                        tvVNum.setText(currUserProfile.getVehicleNumber());
                        tvYear.setText(currUserProfile.getYearOfManufacture());
                        tvTyre.setText(currUserProfile.getTyreSize());
                        tvInsurance.setText(currUserProfile.getInsuranceCompany());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        tvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }
}
