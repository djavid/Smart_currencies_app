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

/**
 * Created by djavid on 16.07.17.
 */


public class CurrenciesAdapter extends ArrayAdapter<String> {
    String[] currs;
    LayoutInflater inflater;

    public CurrenciesAdapter(Context context, int textViewResourceId, String[] objects, LayoutInflater inflater) {
        super(context, textViewResourceId, objects);
        currs = objects;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent, R.layout.row_item);
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
        icon.setImageResource(Codes.getCountryImage(currs[position]));

        return item;
    }
}