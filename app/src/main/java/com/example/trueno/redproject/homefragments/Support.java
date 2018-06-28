package com.example.trueno.redproject.homefragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.trueno.redproject.R;
import com.example.trueno.redproject.models.Feedback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Support extends Fragment {

    Spinner spinner;
    Button btnCall;
    Button btnSubmit;
    EditText etMessage;
    Spinner spnTitle;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    public Support() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);

        spinner = (Spinner) view.findViewById(R.id.feedbacks_spinner);
        btnCall = (Button) view.findViewById(R.id.btnCall);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        spnTitle = view.findViewById(R.id.feedbacks_spinner);
        etMessage = view.findViewById(R.id.etMessage);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("/feedback");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = spnTitle.getSelectedItem().toString();
                String message = etMessage.getText().toString();
                submitFeedback(title,message);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder callDialog = new AlertDialog.Builder(getContext());

                callDialog
                .setMessage("+65 6262 4620")
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "62624620"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = callDialog.create();
                alert.show();
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.feedbacks_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return view;
    }

    public void submitFeedback(String getTitle, String getMessage){
        //Toast.makeText(getActivity(),"Title: "+ title + " Message: "+message,Toast.LENGTH_SHORT).show();

        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        Log.e("USER ID", user.getUid());
        Date currentTime = Calendar.getInstance().getTime();

        String userID = user.getUid() + " (" + currentTime + ")";
        Feedback fbInsert = new Feedback(getTitle,getMessage,currentTime.toString(),"passenger");
        myRef.child(userID).setValue(fbInsert,new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference){
                if (databaseError != null) {
                    Toast.makeText(getActivity(),"Try again later. " + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),"Thank you for your message, we will look into it",Toast.LENGTH_SHORT).show();
                }
            }

        });

//        date.setValue(String.valueOf(currentTime));
//        Log.e("date entered", String.valueOf(currentTime));
//        userRole.setValue("passenger");
//        title.setValue(getTitle);
//        message.setValue(getMessage);

//        myRef.setValue(userid, new DatabaseReference.CompletionListener(){
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                if (databaseError != null) {
//                    Toast.makeText(getActivity(),"Try again later. " + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(),"Thank you for your message, we will look into it",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

}
