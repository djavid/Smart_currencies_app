package com.djavid.bitcoinrate.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.djavid.bitcoinrate.API;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.RateChart;

import at.grabner.circleprogress.CircleProgressView;


public class RateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View view;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    private TextView topPanel, leftPanel;
    private Spinner rightPanel;
    RateChart chart;
    API api;

    private final static String TAG = "MainActivity";
    CircleProgressView mCircleView;
    private SwipeRefreshLayout mSwipeRefreshLayout;


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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rate, container, false);
        layoutInflater = inflater;

        topPanel = (TextView) view.findViewById(R.id.topPanel);
        leftPanel = (TextView) view.findViewById(R.id.leftPanel);
        rightPanel = (Spinner) view.findViewById(R.id.rightPanel);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);

        //rightPanel.setOnClickListener(onRightCurrencySelect);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorOptions));

        mCircleView = (CircleProgressView) view.findViewById(R.id.circleView);
        mCircleView.setSpinSpeed(3);
        mCircleView.setVisibility(View.VISIBLE);
        topPanel.setVisibility(View.GONE);

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);

        spin(true);
        api = new API(getActivity(), view, mCircleView, chart);
        api.viewRate("USD");
        api.viewChart("30days");
        api.getCurrencies();

        return view;
    }

    View.OnClickListener onChartOptionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.optionFirst) {
                api.viewChart("30days");
            } else if (v.getId() == R.id.optionSecond) {
                api.viewChart("90days");
            } else if (v.getId() == R.id.optionThird) {
                api.viewChart("180days");
            } else if (v.getId() == R.id.optionFourth) {
                api.viewChart("1year");
            }
        }
    };


    private void spin(boolean state) {
        if (state) {
            mCircleView.spin();
            mCircleView.setVisibility(View.VISIBLE);
            topPanel.setVisibility(View.GONE);
        } else {
            mCircleView.stopSpinning();
            mCircleView.setVisibility(View.GONE);
            topPanel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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

    @Override
    public void onRefresh() {

//        if (api != null && rightPanel.getSelectedItemPosition() == 0) {
//            api.Refresh(mSwipeRefreshLayout);
//        } else {
//            mSwipeRefreshLayout.setRefreshing(false);
//            Toast.makeText(getActivity(), "Refresh error!", Toast.LENGTH_SHORT).show();
//        }
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


}












