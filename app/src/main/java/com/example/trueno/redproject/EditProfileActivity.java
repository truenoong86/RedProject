package com.example.trueno.redproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trueno.redproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar close;
    EditText etName,etCountry,etPhone,etEmail,etVNumber, etVModel;
    Button saveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference cardDetailsRef = database.getReference("/user").child("passenger");
        final String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        etName = findViewById(R.id.etName);
        etCountry = findViewById(R.id.etCountry);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etVNumber = findViewById(R.id.etVehicleNum);
        etVModel = findViewById(R.id.etVehicleModel);

        saveProfile = findViewById(R.id.saveProfile);
        Log.i("passengerID",passengerId);
        cardDetailsRef.child(passengerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currUserProfile = dataSnapshot.getValue(com.example.trueno.redproject.models.User.class);
                        etName.setText(currUserProfile.getName());
                        etCountry.setText(currUserProfile.getCountryCode());
                        etPhone.setText(currUserProfile.getPhone());
                        etEmail.setText(currUserProfile.getEmail());
                        etVModel.setText(currUserProfile.getVehicleModel());
                        etVNumber.setText(currUserProfile.getVehicleNumber());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DatabaseReference hopperRef = usersRef.child("gracehop");
//                Map<String, Object> hopperUpdates = new HashMap<>();
//                hopperUpdates.put("nickname", "Amazing Grace");
//
//                hopperRef.updateChildrenAsync(hopperUpdates);

                DatabaseReference passengerRef = cardDetailsRef.child(passengerId);
                Map<String, Object> userUpdates = new HashMap<>();
                String uName = etName.getText().toString();
                String uCountryCode = etCountry.getText().toString();
                String uPhone = etPhone.getText().toString();
                String uEmail = etEmail.getText().toString();
                String uVNum = etVNumber.getText().toString();
                String uVModel = etVModel.getText().toString();
                if(uName.length()>0 && uCountryCode.length()>0 && uCountryCode.length()>0 && uPhone.length()>0 && uEmail.length()>0 && uVNum.length()>0 && uVModel.length()>0){
                    userUpdates.put("name",uName);
                    userUpdates.put("countryCode",uCountryCode);
                    userUpdates.put("phone",uPhone);
                    userUpdates.put("email",uEmail);
                    userUpdates.put("vehicleModel",uVModel);
                    userUpdates.put("vehicleNumber",uVNum);
                    passengerRef.updateChildren(userUpdates);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"Missing fields",Toast.LENGTH_SHORT).show();
                }


            }
        });

        close = (android.support.v7.widget.Toolbar) findViewById(R.id.close);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivity.super.onBackPressed();
            }
        });
    }
}
