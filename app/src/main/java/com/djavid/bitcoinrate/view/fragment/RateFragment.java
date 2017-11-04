package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.djavid.bitcoinrate.adapter.CurrenciesAdapter;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.domain.MainRouter;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.RateChart;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

import org.joda.time.LocalDateTime;

import java.sql.Timestamp;

import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;

import static com.djavid.bitcoinrate.util.Codes.country_coins;
import static com.djavid.bitcoinrate.util.Codes.crypto_coins;


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
    String timespan;

    private final static String TAG = "MainActivity";
    private OnFragmentInteractionListener mListener;


    public RateFragment() { }

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
                getResources().getColor(R.color.colorLabelBackground));

        //circleView.setSpinSpeed(3);
        //circleView.setVisibility(View.VISIBLE);
        //topPanel.setVisibility(View.GONE);

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);

        //spin(true);
        setCurrenciesSpinner();

        return view;
    }

    @Override
    public void loadData() {
        //presenter.showChart("30days");
        //timespan = "30days";
        getChart(30);
    }

    private void getChart(int daysAgo) {
        long end = LocalDateTime.now().withHourOfDay(3).plusDays(1).toDateTime().getMillis() / 1000;
        long start = end - 86400 * daysAgo;

        String curr = ((String) leftPanel.getSelectedItem()).toLowerCase() +
                ((String) rightPanel.getSelectedItem()).toLowerCase();

        presenter.getHistory(curr, 86400, start);
    }

    View.OnClickListener onChartOptionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.optionFirst) {
                timespan = "30days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays());
            } else if (v.getId() == R.id.optionSecond) {
                timespan = "90days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays());
            } else if (v.getId() == R.id.optionThird) {
                timespan = "180days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays());
            } else if (v.getId() == R.id.optionFourth) {
                timespan = "1year";
                //presenter.showChart(timespan);
                getChart(getTimespanDays());
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
                    + " must implement OnTickerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        presenter.refresh();
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
    public void setCurrenciesSpinner() {
        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row, crypto_coins,
                getActivity().getLayoutInflater(), R.layout.row_item);
        leftPanel.setAdapter(adapterLeft);
        leftPanel.setOnItemSelectedListener(itemSelectedListener);

        ArrayAdapter<String> adapterRight = new CurrenciesAdapter(getActivity(), R.layout.row, country_coins,
                        getActivity().getLayoutInflater(), R.layout.row_item);
        rightPanel.setAdapter(adapterRight);
        rightPanel.setOnItemSelectedListener(itemSelectedListener);

        leftPanel.setSelection(0);
        rightPanel.setSelection(0);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    presenter.showRate(true); //TODO decide whether to load chart every time
                    //getChart(getTimespanDays());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    @Override
    public Spinner getLeftSpinner() {
        return leftPanel;
    }

    @Override
    public Spinner getRightSpinner() {
        return rightPanel;
    }

    @Override
    public TextView getTopPanel() {
        return topPanel;
    }

    @Override
    public void showProgressbar() {
        super.showProgressbar();
        swipe_container.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
        swipe_container.setVisibility(View.VISIBLE);
    }

    @Override
    public SwipeRefreshLayout getRefreshLayout() {
        return swipe_container;
    }

    @Override
    public RateChart getRateChart() {
        return chart;
    }

    @Override
    public String getSelectedTimespan() {
        return timespan;
    }

    @Override
    public int getTimespanDays() {
        if (timespan == null) timespan = "30days";

        switch (timespan) {
            case "30days":
                return 30;
            case "90days":
                return 90;
            case "180days":
                return 180;
            case "1year":
                return 365;
        }

        return 0;
    }
}












