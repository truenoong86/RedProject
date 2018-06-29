package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trueno.redproject.models.CardDetails;
import com.example.trueno.redproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import br.com.moip.creditcard.Brands;
import br.com.moip.validators.CreditCard;

public class AddCardActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar back;
    public TextView tvTerms;
    EditText etCardNo, etExpiry, etCVV;
    Button btnSubmit;
    Switch swPrimary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        etCardNo = findViewById(R.id.etCardNumber);
        btnSubmit = findViewById(R.id.btnSubmit);
        etCVV = findViewById(R.id.etCVV);
        etExpiry = findViewById(R.id.etExpiry);
        swPrimary = findViewById(R.id.swPrimary);

        tvTerms.setText(Html.fromHtml("<u>Terms & Conditions</u> "));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCardActivity.this, TermsActivity.class));
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardNo = etCardNo.getText().toString();
                String cvvNo = etCVV.getText().toString();
                String expiryNo = etExpiry.getText().toString();

                if((cardNo.length() == 0) && (cvvNo.length() == 0) && (expiryNo.length() == 0)){
                    Toast.makeText(getApplicationContext(),"Please enter details",Toast.LENGTH_SHORT).show();
                }  else {
                    if(cardNo.length() == 16){
                        String stringS = cardNo.toString();
                        stringS.replaceAll(" ","");
                        Brands cardBrand = new CreditCard(stringS).getBrand();


                        if(cardBrand.toString().equalsIgnoreCase("MASTERCARD")){
                            Boolean validity = new CreditCard(stringS).isValid();
                            Log.i("validty",validity+"");
                            if(!validity){
                                Toast.makeText(AddCardActivity.this, "Invalid Credit Card", Toast.LENGTH_SHORT).show();
                            } else {

                                if(cvvNo.length() != 3){
                                    Toast.makeText(AddCardActivity.this, "Invalid CVV number", Toast.LENGTH_SHORT).show();
                                }  else {

                                    if(expiryNo.substring(2,3).equalsIgnoreCase("/")){
                                        int expiryMonth = Integer.parseInt(expiryNo.substring(0,2));
                                        int expiryYear = Integer.parseInt(expiryNo.substring(3));
                                        Calendar cal = Calendar.getInstance();
                                        int calYear = Integer.parseInt((cal.get(Calendar.YEAR)+"").substring(2));
                                        Log.i("compare",expiryYear+" - "+cal.get(Calendar.YEAR));
                                        if(expiryYear>calYear){
                                            //success
                                            if(swPrimary.isChecked()){
                                                addCard(cardNo,cvvNo,expiryNo,true);
                                            } else {
                                                addCard(cardNo,cvvNo,expiryNo,false);
                                            }
                                        } else if (expiryYear == calYear){
                                            if(expiryMonth > cal.get(Calendar.MONTH)){
                                                if(swPrimary.isChecked()){
                                                    addCard(cardNo,cvvNo,expiryNo,true);
                                                } else {
                                                    addCard(cardNo,cvvNo,expiryNo,false);
                                                }
                                            } else {
                                                Toast.makeText(AddCardActivity.this, "Card expired By Month", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(AddCardActivity.this, "Card expired By Year", Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Toast.makeText(AddCardActivity.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid card number",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCardActivity.super.onBackPressed();
            }
        });
    }

    public void addCard(String getCard, String getCVV ,String getDate, Boolean primary){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cardDetailsRef = database.getReference("/cardDetails");
        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(primary){
            cardDetailsRef.child(passengerId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            User currUserProfile = dataSnapshot.getValue(com.example.trueno.redproject.models.User.class);
//                            etName.setText(currUserProfile.getName());
                            if(dataSnapshot.hasChild("primary")){
                                //has existing primary card
                            } else {
                                //no primary card yet
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




        }else{
            CardDetails cardDetails = new CardDetails(getCVV,getDate);
            cardDetailsRef.child(passengerId).child("others").child(getCard).setValue(cardDetails);
            Toast.makeText(getApplicationContext(),"Added Card",Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
