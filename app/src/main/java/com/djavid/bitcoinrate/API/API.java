package com.djavid.bitcoinrate.API;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.CurrenciesAdapter;
import com.djavid.bitcoinrate.Model.BlockchainModel;
import com.djavid.bitcoinrate.Model.CryptonatorTicker;
import com.djavid.bitcoinrate.Model.CurrenciesModel;
import com.djavid.bitcoinrate.Model.Row;
import com.djavid.bitcoinrate.Model.Value;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.RateChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import retrofit2.Call;
import retrofit2.Callback;


public class API {
    private String[] crypto_coins = {"BTC", "LTC", "ETH", "NVC", "NMC", "PPC", "DOGE"};

    private final String TICKER_URL = "https://blockchain.info/ru/ticker";

    private RequestQueue queue;
    private final static String TAG_NULL = "NullPointerException";
    private final static String TAG_FAILURE = "onFailure()";

    private Activity activity;
    private View view;
    private TextView topPanel;
    private Spinner rightPanel, leftPanel;
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
            leftPanel = (Spinner) view.findViewById(R.id.leftPanel);
            rightPanel = (Spinner) view.findViewById(R.id.rightPanel);
            mCircleView = (CircleProgressView) view.findViewById(R.id.circleView);
            mCircleView.setSpinSpeed(3);
        }
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    viewRate();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    private Callback<CryptonatorTicker> getRateCallback = new Callback<CryptonatorTicker>() {
        @Override
        public void onResponse(@NonNull Call<CryptonatorTicker> call,
                               @NonNull retrofit2.Response<CryptonatorTicker> response) {

            try {
                double price = response.body().getTicker().getPrice();

                DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
                symbols.setGroupingSeparator(' ');
                DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
                String text = formatter.format(price) + " " + response.body().getTicker().getTarget();

                if (!topPanel.getText().equals(text)) {
                    topPanel.setText(text);
                }

                mCircleView.stopSpinning();
                mCircleView.setVisibility(View.GONE);
                topPanel.setVisibility(View.VISIBLE);

                if (refreshLayout != null) {
                    if (refreshKeys[1]) refreshLayout.setRefreshing(false);
                    else refreshKeys[0] = true;
                }

            } catch (NullPointerException e) {
                Log.e(TAG_NULL, e.getMessage());
                stopRefresh();
            }

        }

        @Override
        public void onFailure(@NonNull Call<CryptonatorTicker> call, @NonNull Throwable t) {
            Log.e(TAG_FAILURE, t.getMessage());
            stopRefresh();
        }
    };

    private Callback<BlockchainModel> getChartCallback = new Callback<BlockchainModel>() {
        @Override
        public void onResponse(@NonNull Call<BlockchainModel> call,
                               @NonNull retrofit2.Response<BlockchainModel> response) {

            try {
                List<Entry> entries = new ArrayList<Entry>();
                List<Value> values = response.body().getValues();

                for (int i = 0; i < values.size(); i++) {
                    //Date x = new Date(tick.getLong("x") * 1000);
                    long X = values.get(i).getX() * 1000;
                    double y = values.get(i).getY();

                    entries.add(new Entry(X, (float)y));
                }

                int color = activity.getResources().getColor(R.color.colorChart);
                chart.initialize(entries, color);

                if (refreshLayout != null) {
                    if (refreshKeys[0]) refreshLayout.setRefreshing(false);
                    else refreshKeys[1] = true;
                }

            } catch (NullPointerException e) {
                Log.e(TAG_NULL, e.getMessage());
                stopRefresh();
            }

        }

        @Override
        public void onFailure(@NonNull Call<BlockchainModel> call, @NonNull Throwable t) {
            Log.e(TAG_FAILURE, t.getMessage());
            stopRefresh();
        }
    };


    public void viewRate() {
        if (view == null) return;

        final String curr1 = ((Spinner) view.findViewById(R.id.leftPanel)).getSelectedItem().toString();
        final String curr2 = ((Spinner) view.findViewById(R.id.rightPanel)).getSelectedItem().toString();

        App.getCryptonatorApi().getRate(curr1, curr2).enqueue(getRateCallback);
    }

    public void viewChart(String timespan) {
        if (view == null) return;
        this.timespan = timespan;

        App.getBlockchainApi().getChartValues(timespan, true, "json").enqueue(getChartCallback);
    }

    public void getCurrencies() {
        if (view == null) return;

        App.getCryptonatorApi().getCurrencies().enqueue(new Callback<CurrenciesModel>() {
            @Override
            public void onResponse(Call<CurrenciesModel> call, retrofit2.Response<CurrenciesModel> response) {
                List<Row> rows = response.body().getRows();
                for (int i = 0; i < rows.size(); i++) {
                    System.out.print(rows.get(i).getCode() + " ");
                }
                System.out.println();
            }

            @Override
            public void onFailure(Call<CurrenciesModel> call, Throwable t) {

            }
        });

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

                            ArrayAdapter<String> adapterLeft =
                                    new CurrenciesAdapter(activity, R.layout.row, crypto_coins,
                                            activity.getLayoutInflater());
                            leftPanel.setAdapter(adapterLeft);
                            leftPanel.setOnItemSelectedListener(itemSelectedListener);

                            ArrayAdapter<String> adapterRight =
                                    new CurrenciesAdapter(activity, R.layout.row, names,
                                            activity.getLayoutInflater());
                            rightPanel.setAdapter(adapterRight);
                            rightPanel.setOnItemSelectedListener(itemSelectedListener);

                            leftPanel.setSelection(0);
                            rightPanel.setSelection(0);

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

        //Log.e(TAG, message);

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

    public void Refresh(SwipeRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        Arrays.fill(refreshKeys, false);

        viewRate();
        viewChart(timespan);
    }

    private void stopRefresh() {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }

}
