package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.model.API;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.RateChart;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;


public class RateFragment extends BaseFragment implements RateFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.topPanel)
    TextView topPanel;
    @BindView(R.id.leftPanel)
    Spinner leftPanel;
    @BindView(R.id.rightPanel)
    Spinner rightPanel;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;
    @BindView(R.id.circleView)
    CircleProgressView circleView;

    RateFragmentPresenter presenter;

    RateChart chart;
    API api;

    private final static String TAG = "MainActivity";


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
    public void onStart() {
        presenter = getPresenter(RateFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((MainRouter) getActivity());

        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.setView(null);
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View setupView(View view) {

        //rightPanel.setOnClickListener(onRightCurrencySelect);

        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorOptions));

        circleView.setSpinSpeed(3);
        circleView.setVisibility(View.VISIBLE);
        topPanel.setVisibility(View.GONE);

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);

        spin(true);
        api = new API(getActivity(), view, circleView, chart);
        api.getCurrencies();
        //api.viewRate();
        api.viewChart("30days");

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
            circleView.spin();
            circleView.setVisibility(View.VISIBLE);
            topPanel.setVisibility(View.GONE);
        } else {
            circleView.stopSpinning();
            circleView.setVisibility(View.GONE);
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

        if (api != null) { //&& rightPanel.getSelectedItemPosition() == 0
            api.Refresh(swipe_container);
        } else {
            swipe_container.setRefreshing(false);
            Toast.makeText(getActivity(), "Refresh error!", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rate;
    }

    @Override
    public String getPresenterId() {
        return "rate_fragment";
    }

    @Override
    public void loadData() {

    }
}












