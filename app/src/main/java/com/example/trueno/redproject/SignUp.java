package com.example.trueno.redproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trueno.redproject.models.User;
import com.example.trueno.redproject.services.CustomOnItemSelectedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity {
    public static final String TAG = "SignUp.java";

    private android.support.v7.widget.Toolbar back;
    public TextView tvTerms, tvPrivacyPolicy;
    public EditText editTextUserEmail, editTextPassword, editTextConfirmPassword, etCountryCode, etPhone, etNumber, etName;
    public Button buttonSignUp;
    public Spinner spnMake, spnModel, spnYear, spnTyre, spnInsurance;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userListRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.d(TAG, "onCreate: ");
        
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        userListRef = firebaseDatabase.getReference("/user/passenger");

        back = (android.support.v7.widget.Toolbar) findViewById(R.id.back);
        tvTerms = (TextView) findViewById(R.id.tvTerms);
        tvPrivacyPolicy = (TextView) findViewById(R.id.tvPrivacyPolicy);
        editTextUserEmail = (EditText) findViewById(R.id.etUserEmail);
        editTextPassword = (EditText) findViewById(R.id.etUserPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.etUserConfirmPassword);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);

        etName = findViewById(R.id.etName);
        etCountryCode = findViewById(R.id.etCountryCode);
          etNumber = findViewById(R.id.etVehicleNumber);
          etPhone = findViewById(R.id.etPhoneNumber);
//        etModel = findViewById(R.id.etVehicleModel);
//        etYear = findViewById(R.id.etYearOfManufacture);
//        etTyre = findViewById(R.id.etTyreSize);
//        etInsurance = findViewById(R.id.etInsurance);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.super.onBackPressed();
            }
        });

        tvTerms.setText(Html.fromHtml("<u>Terms of Use</u> "));
        tvPrivacyPolicy.setText(Html.fromHtml("<u>Privacy Policy</u>"));

        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, TermsActivity.class));
            }
        });

        tvPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, PrivacyPolicyActivity.class));
            }
        });

        addItemsOnSpinners();
        //addListenerOnSpinnerItemSelection();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSignUp();
            }
        });


    }

    private void buttonSignUp() {
        String userEmail = editTextUserEmail.getText().toString();
        String userPassword = editTextPassword.getText().toString();
        String userConfirmPassword = editTextConfirmPassword.getText().toString();

        String userName = etName.getText().toString();

        spnMake = findViewById(R.id.spnVehicleMake);
        spnModel = findViewById(R.id.spnVehicleModel);
        spnYear = findViewById(R.id.spnYearOfManufacture);
        spnTyre = findViewById(R.id.spnTyreSize);
        spnInsurance = findViewById(R.id.spnInsurance);

        final String vMake = String.valueOf(spnMake.getSelectedItem());
        final String vModel = String.valueOf(spnModel.getSelectedItem());
        final String vYear = String.valueOf(spnYear.getSelectedItem());
        final String vTyre = String.valueOf(spnTyre.getSelectedItem());
        final String vInsurance = String.valueOf(spnInsurance.getSelectedItem());


            firebaseAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            User user =new User(etName.getText().toString(), etCountryCode.getText().toString() , etPhone.getText().toString(), editTextUserEmail.getText().toString(), etNumber.getText().toString(),vMake,vModel,vTyre, vInsurance, vYear);

                            String id = currentUser.getUid();
                            userListRef.child(id).setValue(user);
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Registration fail",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });


    }

    private void updateUI(FirebaseUser user) {
    }


    public void addListenerOnSpinnerItemSelection() {
        spnMake = (Spinner) findViewById(R.id.spnVehicleMake);
        spnMake.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spnModel = (Spinner) findViewById(R.id.spnVehicleModel);
        spnModel.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spnYear = (Spinner) findViewById(R.id.spnYearOfManufacture);
        spnYear.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spnTyre = (Spinner) findViewById(R.id.spnTyreSize);
        spnTyre.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        spnInsurance = (Spinner) findViewById(R.id.spnInsurance);
        spnInsurance.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }



    public void addItemsOnSpinners(){

        spnMake = findViewById(R.id.spnVehicleMake);
        spnModel = findViewById(R.id.spnVehicleModel);
        spnYear = findViewById(R.id.spnYearOfManufacture);
        spnTyre = findViewById(R.id.spnTyreSize);
        spnInsurance = findViewById(R.id.spnInsurance);

        List<String> listMake = new ArrayList<String>();
        listMake.add("Toyota");
        listMake.add("Volvo");
        listMake.add("BMW");
        listMake.add("Nissan");
        listMake.add("Honda");
        listMake.add("Mercedez");
        listMake.add("Kia");
        listMake.add("Mitsubishi");
        ArrayAdapter<String> dataMakeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listMake);
        dataMakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMake.setAdapter(dataMakeAdapter);

        List<String> listModel = new ArrayList<String>();
        listModel.add("Test Model 1");
        listModel.add("Test Model 2");
        listModel.add("Test Model 3");
        listModel.add("Test Model 4");
        listModel.add("Test Model 5");
        ArrayAdapter<String> dataModelAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listModel);
        dataModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnModel.setAdapter(dataModelAdapter);

        List<String> listYear = new ArrayList<String>();
        listYear.add("1990");
        listYear.add("1991");
        listYear.add("1992");
        listYear.add("1993");
        listYear.add("1994");
        listYear.add("1995");
        listYear.add("1996");
        listYear.add("1997");
        listYear.add("1998");
        listYear.add("1999");
        listYear.add("2000");
        listYear.add("2001");
        listYear.add("2002");
        listYear.add("2003");
        listYear.add("2004");
        listYear.add("2005");
        listYear.add("2006");
        listYear.add("2007");
        listYear.add("2008");
        listYear.add("2009");
        listYear.add("2010");
        listYear.add("2011");
        listYear.add("2012");
        listYear.add("2013");
        listYear.add("2014");
        listYear.add("2015");
        listYear.add("2016");
        listYear.add("2017");
        listYear.add("2018");
        ArrayAdapter<String> dataYearAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listYear);
        dataYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnYear.setAdapter(dataYearAdapter);

        List<String> listTyre = new ArrayList<String>();
        listTyre.add("205/40R15");
        listTyre.add("205/40R16");
        listTyre.add("205/40R17");
        listTyre.add("205/40R18");
        listTyre.add("205/40R19");
        listTyre.add("205/40R20");
        listTyre.add("205/40R21");
        listTyre.add("205/40R22");
        ArrayAdapter<String> dataTyreAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTyre);
        dataTyreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTyre.setAdapter(dataTyreAdapter);


        List<String> listInsurance = new ArrayList<String>();
        listInsurance.add("Aviva Car");
        listInsurance.add("Citibank SmartDrive");
        listInsurance.add("AIG AutoPlus Car");
        listInsurance.add("HSBC SmartDrive Car");
        listInsurance.add("POSB DriveShield Car");
        listInsurance.add("DBS DriveShield Car");
        ArrayAdapter<String> dataInsuranceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listInsurance);
        dataTyreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInsurance.setAdapter(dataInsuranceAdapter);


    }
}
