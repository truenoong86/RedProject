package com.example.trueno.redproject;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trueno.redproject.models.Services;

import java.util.List;

class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private List<Services> mServicesList;

    public CustomAdapter(Context mContext, List<Services> mServicesList) {
        this.mContext = mContext;
        this.mServicesList = mServicesList;
    }

    @Override
    public int getCount() {
        return mServicesList.size();
    }

    @Override
    public Object getItem(int i) {
        return mServicesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.custom_row, null);
        TextView tvServices = (TextView)v.findViewById(R.id.tvServices);
        TextView tvPrice = (TextView)v.findViewById(R.id.tvPrice);

        tvServices.setText(mServicesList.get(position).getService());
        tvPrice.setText("$" + Double.valueOf(mServicesList.get(position).getDay_price()));

        return v;
    }
}
