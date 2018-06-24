package com.example.trueno.redproject.homefragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.trueno.redproject.AddCardActivity;
import com.example.trueno.redproject.CardSettingsActivity;
import com.example.trueno.redproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Card extends Fragment {

    LinearLayout cardSettings;
    Button btnAddCard;

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

        cardSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CardSettingsActivity.class));
            }
        });

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddCardActivity.class));
            }
        });

        return view;
    }

}
