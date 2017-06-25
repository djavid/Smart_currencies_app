package com.djavid.bitcoinrate;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
    private LineChart chart;
    CircleProgressView mCircleView;


    public API(Activity activity, View view, CircleProgressView circle) {
        queue = Volley.newRequestQueue(activity);
        this.activity = activity;
        this.view = view;
        mCircleView = circle;

        if (view != null) {
            topPanel = (TextView) view.findViewById(R.id.topPanel);
            leftPanel = (TextView) view.findViewById(R.id.leftPanel);
            rightPanel = (Spinner) view.findViewById(R.id.rightPanel);
            chart = (LineChart) view.findViewById(R.id.chart);
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                        System.out.println(message);
                    }
                });

        queue.add(jsonRequest);
    }

    public void viewChart(String timespan) {
        if (view == null) return;

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
                                Date x = new Date(tick.getLong("x") * 1000);
                                double y = tick.getDouble("y");

                                entries.add(new Entry(i, (float)y));
                            }

                            LineDataSet dataSet = new LineDataSet(entries, "");
                            int color = activity.getResources().getColor(R.color.colorChart);
                            dataSet.setColor(color);
                            dataSet.setDrawCircles(false);
                            dataSet.setDrawValues(false);
                            dataSet.setLineWidth(4);


                            LineData lineData = new LineData(dataSet);
                            chart.setData(lineData);
                            Description desc = new Description();
                            desc.setText("");
                            chart.setDescription(desc);
                            chart.getLegend().setEnabled(false);
                            chart.getAxisRight().setDrawLabels(false);
                            chart.invalidate();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                                    R.layout.currency_spinner, names);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rightPanel.setAdapter(adapter);
                            rightPanel.setOnItemSelectedListener(itemSelectedListener);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });


        queue.add(jsonRequest);
    }
}
