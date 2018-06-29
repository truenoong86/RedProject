package com.example.trueno.redproject.homefragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trueno.redproject.AddCardActivity;
import com.example.trueno.redproject.CardSettingsActivity;
import com.example.trueno.redproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class Card extends Fragment {

    LinearLayout cardSettings;
    Button btnAddCard;
    TextView tvLast4;

    public Card() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        cardSettings = (LinearLayout) view.findViewById(R.id.cardSettings);
        btnAddCard = (Button) view.findViewById(R.id.btnAddCard);
        tvLast4 = view.findViewById(R.id.tvLast4);

        btnAddCard.setVisibility(View.INVISIBLE);
        cardSettings.setVisibility(View.INVISIBLE);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference passengerRef = database.getReference("/user").child("passenger");
        String passengerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cardDetailsRef = passengerRef.child(passengerId);

        cardDetailsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("cardDetails").exists()){
                        cardSettings.setVisibility(View.VISIBLE);
                        String cardNo = dataSnapshot.child("cardDetails").child("cardNo").getValue().toString();
                        String last4 = cardNo.substring(12);
                        tvLast4.setText(last4);
                    } else {
                        btnAddCard.setVisibility(View.VISIBLE);
                    }
                } else {
                    btnAddCard.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), CardSettingsActivity.class));
                startActivityForResult(new Intent(getActivity(), CardSettingsActivity.class),0000);
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getActivity(), AddCardActivity.class));
                startActivityForResult(new Intent(getActivity(), AddCardActivity.class),1111);
            }
        });

        return view;
    }


    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

}
