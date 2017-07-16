package com.djavid.bitcoinrate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    View getCustomView(int position, View convertView, ViewGroup parent, int resource) {
        View item = inflater.inflate(resource, parent, false);

        TextView name = (TextView) item.findViewById(R.id.curr_name);
        ImageView icon = (ImageView) item.findViewById(R.id.curr_icon);

        name.setText(currs[position]);
        switch (currs[position]) {
            case "RUB":
                icon.setImageResource(R.drawable.ic_russia);
                break;
            case "USD":
                icon.setImageResource(R.drawable.ic_united_states_of_america);
                break;
            case "EUR":
                icon.setImageResource(R.drawable.ic_european_union);
                break;
            case "ISK":
                icon.setImageResource(R.drawable.ic_iceland);
                break;
            case "HKD":
                icon.setImageResource(R.drawable.ic_hong_kong);
                break;
            case "TWD":
                icon.setImageResource(R.drawable.ic_taiwan);
                break;
            case "CHF":
                icon.setImageResource(R.drawable.ic_switzerland);
                break;
            case "DKK":
                icon.setImageResource(R.drawable.ic_denmark);
                break;
            case "CLP":
                icon.setImageResource(R.drawable.ic_chile);
                break;
            case "CAD":
                icon.setImageResource(R.drawable.ic_canada);
                break;
            case "INR":
                icon.setImageResource(R.drawable.ic_india);
                break;
            case "CNY":
                icon.setImageResource(R.drawable.ic_china);
                break;
            case "THB":
                icon.setImageResource(R.drawable.ic_thailand);
                break;
            case "AUD":
                icon.setImageResource(R.drawable.ic_australia);
                break;
            case "SGD":
                icon.setImageResource(R.drawable.ic_singapore);
                break;
            case "KRW":
                icon.setImageResource(R.drawable.ic_south_korea);
                break;
            case "JPY":
                icon.setImageResource(R.drawable.ic_japan);
                break;
            case "PLN":
                icon.setImageResource(R.drawable.ic_republic_of_poland);
                break;
            case "GBP":
                icon.setImageResource(R.drawable.ic_united_kingdom);
                break;
            case "SEK":
                icon.setImageResource(R.drawable.ic_sweden);
                break;
            case "NZD":
                icon.setImageResource(R.drawable.ic_new_zealand);
                break;
            case "BRL":
                icon.setImageResource(R.drawable.ic_brazil);
                break;
            case "BTC":
                icon.setImageResource(R.drawable.ic_bitcoin);
                break;
            case "LTC":
                icon.setImageResource(R.drawable.ic_litecoin);
                break;
            case "NMC":
                icon.setImageResource(R.drawable.ic_namecoin);
                break;
            case "DOGE":
                icon.setImageResource(R.drawable.ic_dogecoin);
                break;
            default:
                icon.setImageResource(R.drawable.ic_european_union);
                break;
        }

        return item;
    }
}
