package com.example.trueno.redproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trueno.redproject.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class EditProfileActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar close;
    EditText etName,etCountry,etPhone,etEmail,etVNumber, etVModel;
    Button saveProfile;
    CircleImageView civProfile;

    Spinner spnMake, spnModel, spnYear, spnTyre,spnInsurance;

    ArrayAdapter<String> dataMakeAdapter;
    ArrayAdapter<String> dataModelAdapter;
    ArrayAdapter<String> dataYearAdapter;
    ArrayAdapter<String> dataTyreAdapter;
    ArrayAdapter<String> dataInsuranceAdapter;

    private Uri resultUri;
    private Bitmap resultBitmap;

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
        //etVModel = findViewById(R.id.etVehicleModel);

        spnMake = findViewById(R.id.spnVehicleMake);
        spnModel = findViewById(R.id.spnVehicleModel);
        spnYear = findViewById(R.id.spnYearOfManufacture);
        spnTyre = findViewById(R.id.spnTyreSize);
        spnInsurance = findViewById(R.id.spnInsurance);

        civProfile = findViewById(R.id.civProfile);

        saveProfile = findViewById(R.id.saveProfile);
        Log.i("passengerID",passengerId);


        addItemsOnSpinners();

        //circleimageview
        civProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,1007);
            }
        });


        cardDetailsRef.child(passengerId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User currUserProfile = dataSnapshot.getValue(com.example.trueno.redproject.models.User.class);
                        etName.setText(currUserProfile.getName());
                        etCountry.setText(currUserProfile.getCountryCode());
                        etPhone.setText(currUserProfile.getPhone());
                        etEmail.setText(currUserProfile.getEmail());
                        etVNumber.setText(currUserProfile.getVehicleNumber());
                        Log.i("position",dataMakeAdapter.getPosition(currUserProfile.getVehicleMake())+"");
                        Log.i("position",dataModelAdapter.getPosition(currUserProfile.getVehicleModel())+"");
                        Log.i("position",dataTyreAdapter.getPosition(currUserProfile.getTyreSize())+"");
                        Log.i("position",dataYearAdapter.getPosition(currUserProfile.getYearOfManufacture())+"");
                        Log.i("position",dataInsuranceAdapter.getPosition(currUserProfile.getInsuranceCompany())+"");
                        spnMake.setSelection(dataMakeAdapter.getPosition(currUserProfile.getVehicleMake()));
                        spnModel.setSelection(dataModelAdapter.getPosition(currUserProfile.getVehicleModel()));
                        spnTyre.setSelection(dataTyreAdapter.getPosition(currUserProfile.getTyreSize()));
                        spnYear.setSelection(dataYearAdapter.getPosition(currUserProfile.getYearOfManufacture()));
                        spnInsurance.setSelection(dataInsuranceAdapter.getPosition(currUserProfile.getInsuranceCompany()));
                        if(dataSnapshot.hasChild("profileImageUrl")){
                            if(!currUserProfile.getProfileImageUrl().equalsIgnoreCase("No Image")){
                                Glide.with(getApplication()).load(currUserProfile.getProfileImageUrl()).into(civProfile);
                            }
                        }
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


                final DatabaseReference passengerRef = cardDetailsRef.child(passengerId);
                final Map<String, Object> userUpdates = new HashMap<>();
                String uName = etName.getText().toString();
                String uCountryCode = etCountry.getText().toString();
                String uPhone = etPhone.getText().toString();
                String uEmail = etEmail.getText().toString();
                String uVNum = etVNumber.getText().toString();
                //String uVModel = etVModel.getText().toString();

                String vMake = String.valueOf(spnMake.getSelectedItem());
                String vModel = String.valueOf(spnModel.getSelectedItem());
                String vYear = String.valueOf(spnYear.getSelectedItem());
                String vTyre = String.valueOf(spnTyre.getSelectedItem());
                String vInsurance = String.valueOf(spnInsurance.getSelectedItem());


                if(uName.length()>0 && uCountryCode.length()>0 && uCountryCode.length()>0 && uPhone.length()>0 && uEmail.length()>0 && uVNum.length()>0){
                    userUpdates.put("name",uName);
                    userUpdates.put("countryCode",uCountryCode);
                    userUpdates.put("phone",uPhone);
                    userUpdates.put("email",uEmail);
                    userUpdates.put("vehicleNumber",uVNum);
                    userUpdates.put("vehicleMake",vMake);
                    userUpdates.put("vehicleModel",vModel);
                    userUpdates.put("yearOfManufacture",vYear);
                    userUpdates.put("tyreSize",vTyre);
                    userUpdates.put("insuranceCompany",vInsurance);




                    passengerRef.updateChildren(userUpdates);

                    //checkImageAvailable
                    if(resultUri != null || resultBitmap != null){
                        Log.i("status called","resultUri is not null");
                        StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profile_image").child(passengerId);
                        Bitmap bitmap = null;

                        try{
                            if(resultUri != null){
                                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(),resultUri);
                            }else if(resultBitmap != null){
                                bitmap = resultBitmap;
                            }
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = filePath.putBytes(data);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.i("status called","success added");
                                StorageMetadata smd = taskSnapshot.getMetadata();
                                StorageReference sRef = smd.getReference();
                                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Uri downloadUrl = uri;
                                        //Map newImage = new HashMap();
                                        userUpdates.put("profileImageUrl",downloadUrl.toString());
                                        passengerRef.updateChildren(userUpdates);
                                        finish();
                                    }
                                });

                            }
                        });

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed to upload image",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
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
        dataMakeAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listMake);
        dataMakeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMake.setAdapter(dataMakeAdapter);


        List<String> listModel = new ArrayList<String>();
        listModel.add("Test Model 1");
        listModel.add("Test Model 2");
        listModel.add("Test Model 3");
        listModel.add("Test Model 4");
        listModel.add("Test Model 5");
        dataModelAdapter = new ArrayAdapter<String>(this,
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
        dataYearAdapter = new ArrayAdapter<String>(this,
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
        dataTyreAdapter = new ArrayAdapter<String>(this,
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
        dataInsuranceAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listInsurance);
        dataTyreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnInsurance.setAdapter(dataInsuranceAdapter);


    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

//    Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//    startActivityForResult(intent,1007);

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1007);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1008);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1007 &&resultCode == Activity.RESULT_OK){
            final Uri imageGalleryUri = data.getData();
            resultUri = imageGalleryUri;
            resultBitmap = null;
            civProfile.setImageURI(imageGalleryUri);
        } else if(requestCode == 1008 &&resultCode == Activity.RESULT_OK){
            Bundle extras = data.getExtras();
            resultBitmap = (Bitmap) extras.get("data");
            resultUri = null;
            civProfile.setImageBitmap(resultBitmap);
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
        }
    }
}
