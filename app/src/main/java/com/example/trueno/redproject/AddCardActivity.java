package com.example.trueno.redproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.com.moip.creditcard.Brands;
import br.com.moip.validators.CreditCard;

public class AddCardActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar back;
    public TextView tvTerms;
    EditText etCardNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        etCardNo = findViewById(R.id.etCardNumber);

        tvTerms.setText(Html.fromHtml("<u>Terms & Conditions</u> "));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCardActivity.this, TermsActivity.class));
            }
        });

        etCardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 16){
                    String stringS = s.toString();
                    stringS.replaceAll(" ","");
                    Brands cardBrand = new CreditCard(stringS).getBrand();
                    Log.i("masterCard",cardBrand.toString());
                    if(cardBrand.toString().equalsIgnoreCase("MASTERCARD")){
                        Boolean validity = new CreditCard(stringS).isValid();
                        Log.i("validty",validity+"");
                        if(validity){
                            Toast.makeText(AddCardActivity.this,"Valid Credit Card",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddCardActivity.this, "Invalid Credit Card", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddCardActivity.super.onBackPressed();
            }
        });
    }
}
