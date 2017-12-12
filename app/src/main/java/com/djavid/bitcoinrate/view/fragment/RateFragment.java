package com.djavid.bitcoinrate.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
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

    OnFragmentInteractionListener mListener;
    RateFragmentPresenter presenter;
    RateChart chart;
    String timespan;


    public RateFragment() { }
    public static RateFragment newInstance() {
        return new RateFragment();
    }


    @Override
    public void loadData() {
//        if (!App.getAppInstance().getPrefencesWrapper().sharedPreferences.contains("token_id")) {
//            presenter.sendTokenToServer();
//        }
    }

    @Override
    public View setupView(View view) {

        swipe_container.setOnRefreshListener(this);
        swipe_container.setColorSchemeColors(
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorChart),
                getResources().getColor(R.color.colorLabelBackground));

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);

        setCurrenciesSpinner();

        return view;
    }

    @Override
    public void setCurrenciesSpinner() {
        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row, crypto_coins,
                getActivity().getLayoutInflater(), R.layout.row_item);
        leftPanel.setAdapter(adapterLeft);
        leftPanel.setSelection(0, false);
        leftPanel.setOnItemSelectedListener(itemSelectedListener);

        ArrayAdapter<String> adapterRight = new CurrenciesAdapter(getActivity(), R.layout.row, country_coins,
                        getActivity().getLayoutInflater(), R.layout.row_item);
        rightPanel.setAdapter(adapterRight);
        rightPanel.setSelection(0);
        rightPanel.setOnItemSelectedListener(itemSelectedListener);

//        leftPanel.setSelection(0);
//        rightPanel.setSelection(0);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //TODO decide whether to load chart every time
                    presenter.showRate(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    View.OnClickListener onChartOptionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.optionFirst) {
                timespan = "30days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays(), 7200);
            } else if (v.getId() == R.id.optionSecond) {
                timespan = "90days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays(), 21600);
            } else if (v.getId() == R.id.optionThird) {
                timespan = "180days";
                //presenter.showChart(timespan);
                getChart(getTimespanDays(), 43200);
            } else if (v.getId() == R.id.optionFourth) {
                timespan = "1year";
                //presenter.showChart(timespan);
                getChart(getTimespanDays(), 86400);
            }
        }
    };

    private void getChart(int daysAgo, int periods) {
        long end = LocalDateTime.now().withHourOfDay(3).plusDays(1).toDateTime().getMillis() / 1000;
        long start = end - 86400 * daysAgo;

        String curr = ((String) leftPanel.getSelectedItem()).toLowerCase() +
                ((String) rightPanel.getSelectedItem()).toLowerCase();

        presenter.getHistory(curr, periods, start);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onStart() {
        presenter = getPresenter(RateFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((MainRouter) getActivity());
        presenter.onStart();

        super.onStart();
    }

    @Override
    public void onStop() {
        presenter.setView(null);
        presenter.onStop();

        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rate;
    }

    @Override
    public String getPresenterId() {
        return "rate_fragment";
    }

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












