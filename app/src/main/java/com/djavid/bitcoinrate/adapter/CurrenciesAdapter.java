package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.util.Codes;


public class CurrenciesAdapter extends ArrayAdapter<String> {
    private String[] currs;
    private LayoutInflater inflater;
    private int rowItemId;

    public CurrenciesAdapter(Context context, int textViewResourceId, String[] objects,
                             LayoutInflater inflater, int rowItemId) {
        super(context, textViewResourceId, objects);
        currs = objects;
        this.inflater = inflater;
        this.rowItemId = rowItemId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, rowItemId);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, R.layout.row);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, int resource) {
        View item = inflater.inflate(resource, parent, false);

        TextView name = (TextView) item.findViewById(R.id.curr_name);
        ImageView icon = (ImageView) item.findViewById(R.id.curr_icon);

        name.setText(currs[position]);
        icon.setImageResource(Codes.getCurrencyImage(currs[position]));

        return item;
    }
}
