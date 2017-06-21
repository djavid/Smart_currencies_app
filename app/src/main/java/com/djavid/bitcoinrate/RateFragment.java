package com.djavid.bitcoinrate;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RateFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private View view;
    private TextView topPanel, leftPanel;
    private Spinner rightPanel;
    LineChart chart;


    private OnFragmentInteractionListener mListener;

    public RateFragment() {
        // Required empty public constructor
    }

    public static RateFragment newInstance() {
        RateFragment fragment = new RateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rate, container, false);

        topPanel = (TextView) view.findViewById(R.id.topPanel);
        leftPanel = (TextView) view.findViewById(R.id.leftPanel);
        rightPanel = (Spinner) view.findViewById(R.id.rightPanel);
        chart = (LineChart) view.findViewById(R.id.chart);

        viewRate("USD");
        viewChart("30days");
        getCurrencies();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.refresh){
            Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
            viewRate(rightPanel.getSelectedItem().toString());

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void viewRate(final String currency) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://blockchain.info/ru/ticker";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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

    private void viewChart(String timespan) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                            int color = getResources().getColor(R.color.colorChart);
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

    private void getCurrencies() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://blockchain.info/ru/ticker";

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray keys = response.names();
                            String[] names = new String[keys.length()];

                            for (int i = 0; i < names.length; i++) {
                                names[i] = keys.getString(i);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
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












