package com.example.trueno.redproject.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.trueno.redproject.R;

public class HistoryViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView tvDate,tvFrom,tvTo;


    public HistoryViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        tvDate = itemView.findViewById(R.id.tvDateTime);
        tvFrom = itemView.findViewById(R.id.tvFromAddress);
        tvTo = itemView.findViewById(R.id.tvToAddress);

    }

    @Override
    public void onClick(View v){

    }



}
