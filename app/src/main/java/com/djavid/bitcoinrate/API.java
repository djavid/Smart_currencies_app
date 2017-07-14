package com.djavid.bitcoinrate;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.djavid.bitcoinrate.Fragments.RateFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;


public class API {
    private final String TICKER_URL = "https://blockchain.info/ru/ticker";
    private RequestQueue queue;
    private final static String TAG = "API class";

    private Activity activity;
    private View view;
    private TextView topPanel, leftPanel;
    private Spinner rightPanel;
    CircleProgressView mCircleView;
    private RateChart chart;
    String timespan;
    SwipeRefreshLayout refreshLayout;
    boolean[] refreshKeys;


    public API(Activity activity, View view, CircleProgressView circle, RateChart chart) {
        queue = Volley.newRequestQueue(activity);
        this.activity = activity;
        this.view = view;
        mCircleView = circle;
        this.chart = chart;
        timespan = "30days";
        refreshKeys = new boolean[2];

        if (view != null) {
            topPanel = (TextView) view.findViewById(R.id.topPanel);
            leftPanel = (TextView) view.findViewById(R.id.leftPanel);
            rightPanel = (Spinner) view.findViewById(R.id.rightPanel);
            mCircleView = (CircleProgressView) view.findViewById(R.id.circleView);
            mCircleView.setSpinSpeed(3);
        }
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String curr = (String)parent.getItemAtPosition(position);
                    viewRate(curr);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    public void viewRate(final String currency) {
        if (view == null) return;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, TICKER_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject ticker = response.getJSONObject(currency);
                            double price = ticker.getDouble("last");

                            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                            symbols.setGroupingSeparator(' ');
                            DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
                            String text2 = formatter.format(price) + " " + currency;

                            if (!topPanel.getText().equals(text2)) {
                                topPanel.setText(text2);
                            }

                            mCircleView.stopSpinning();
                            mCircleView.setVisibility(View.GONE);
                            topPanel.setVisibility(View.VISIBLE);

                            if (refreshLayout != null) {
                                if (refreshKeys[1]) refreshLayout.setRefreshing(false);
                                else refreshKeys[0] = true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler(error);
                    }
                });

        queue.add(jsonRequest);
    }

    public void viewChart(String timespan) {
        if (view == null) return;
        this.timespan = timespan;

        String url = "https://blockchain.info/ru/charts/market-price?"
                + "timespan=" + timespan + "&" + "sampled=true&" + "format=json";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray values = response.getJSONArray("values");
                            List<Entry> entries = new ArrayList<Entry>();

                            for (int i = 0; i < values.length(); i++) {
                                JSONObject tick = values.getJSONObject(i);
                                //Date x = new Date(tick.getLong("x") * 1000);
                                long X = tick.getLong("x") * 1000;
                                double y = tick.getDouble("y");

                                entries.add(new Entry(X, (float)y));
                            }

                            int color = activity.getResources().getColor(R.color.colorChart);
                            chart.initialize(entries, color);

                            if (refreshLayout != null) {
                                if (refreshKeys[0]) refreshLayout.setRefreshing(false);
                                else refreshKeys[1] = true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler(error);
                    }
                });

        queue.add(jsonRequest);
    }

    public void getCurrencies() {
        if (view == null) return;

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, TICKER_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray keys = response.names();
                            String[] names = new String[keys.length()];

                            for (int i = 0; i < names.length; i++) {
                                names[i] = keys.getString(i);
                            }

                            ArrayAdapter<String> adapter =
                                    new CurrenciesAdapter(activity, R.layout.row, names);

                            rightPanel.setAdapter(adapter);
                            rightPanel.setOnItemSelectedListener(itemSelectedListener);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorHandler(error);
                    }
                });


        queue.add(jsonRequest);
    }

    private void errorHandler(VolleyError error) {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }

        Log.e(TAG, message);

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    public void Refresh(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        Arrays.fill(refreshKeys, false);

        viewRate(rightPanel.getSelectedItem().toString());
        viewChart(timespan);
    }

    private class CurrenciesAdapter extends ArrayAdapter<String> {
        String[] currs;

        CurrenciesAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            currs = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = activity.getLayoutInflater();
            View item = inflater.inflate(R.layout.row, parent, false);

            TextView name = (TextView) item.findViewById(R.id.curr_name);
            ImageView icon = (ImageView) item.findViewById(R.id.curr_icon);

            name.setText(currs[position]);
            if (currs[position].equals("RUB")) {
                icon.setImageResource(R.drawable.ic_russia);
            } else if (currs[position].equals("USD")) {
                icon.setImageResource(R.drawable.ic_united_states_of_america);
            } else if (currs[position].equals("EUR")) {
                icon.setImageResource(R.drawable.ic_european_union);
            } else if (currs[position].equals("ISK")) {
                icon.setImageResource(R.drawable.ic_iceland);
            } else if (currs[position].equals("HKD")) {
                icon.setImageResource(R.drawable.ic_hong_kong);
            } else if (currs[position].equals("TWD")) {
                icon.setImageResource(R.drawable.ic_taiwan);
            } else {
                icon.setImageResource(R.drawable.ic_european_union);
            }

            return item;
        }
    }
}
