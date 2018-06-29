package com.example.trueno.redproject.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trueno.redproject.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolders> {

    private ArrayList<HistoryObject> itemList;
    private Context context;

    public HistoryAdapter(ArrayList<HistoryObject> itemList, Context context){
        this.itemList = itemList;
        this.context = context;
    }
    @NonNull
    @Override
    public HistoryViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        HistoryViewHolders rcv = new HistoryViewHolders(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolders holder, int position) {

        //holder.rideId.setText(itemList.get(position).getRideId());
        holder.tvDate.setText(itemList.get(position).getTimestamp());
        holder.tvFrom.setText(itemList.get(position).getFromAddress());
        holder.tvTo.setText(itemList.get(position).getToAddress());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
