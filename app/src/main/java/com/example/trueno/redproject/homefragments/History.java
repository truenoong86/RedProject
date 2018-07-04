package com.example.trueno.redproject.homefragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trueno.redproject.R;
import com.example.trueno.redproject.models.HistoryAdapter;
import com.example.trueno.redproject.models.HistoryObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {

    Context mContext;

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    String userId;

    public History() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mHistoryRecyclerView = rootView.findViewById(R.id.historyRecyclerView);
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);

        mHistoryLayoutManager = new LinearLayoutManager(mContext);
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), mContext);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistory();


        mHistoryAdapter.notifyDataSetChanged();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void getUserHistory(){
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("history");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot history : dataSnapshot.getChildren()){
                        Log.i("data key", history.getKey());

                        if(history.child("passenger_uid").getValue().toString().equalsIgnoreCase(userId)){
                            String passengerID = history.child("passenger_uid").getValue().toString();
                            Log.i("data passengerID",passengerID);
                            String from = history.child("pickup_name").getValue().toString();
                            String to = history.child("destination_name").getValue().toString();
                            Log.i("data location",from + " " + to);
                            String rating = history.child("rating").getValue().toString();
                            Log.i("data rating",from + " " + to);
                            String timestamp = history.child("transaction_time_complete").getValue().toString();
                            Log.i("data timestmap", timestamp);

                            HistoryObject obj = new HistoryObject(history.getKey(),passengerID,from,to,rating,timestamp);
                            resultsHistory.add(obj);
                            mHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList resultsHistory = new ArrayList();
    private ArrayList<HistoryObject> getDataSetHistory(){
        return resultsHistory;
    }





}
