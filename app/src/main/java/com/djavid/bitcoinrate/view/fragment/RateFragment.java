package com.djavid.bitcoinrate.view.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseFragment;
import com.djavid.bitcoinrate.core.Router;
import com.djavid.bitcoinrate.model.project.ChartOption;
import com.djavid.bitcoinrate.model.project.Coin;
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState;
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.RateChart;
import com.djavid.bitcoinrate.view.dialog.SearchDialog;
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import static com.djavid.bitcoinrate.util.Config.chart_options;
import static com.djavid.bitcoinrate.util.Config.country_coins;
import static com.djavid.bitcoinrate.util.Config.crypto_coins;


public class RateFragment extends BaseFragment implements RateFragmentView
//        , SwipeRefreshLayout.OnRefreshListener
        {

//    @BindView(R.id.swipe_container)
//    SwipeRefreshLayout swipe_container;

    @BindView(R.id.topPanel)
    TextView topPanel;

    @BindView(R.id.left_panel)
    LinearLayout left_panel;
    @BindView(R.id.right_panel)
    LinearLayout right_panel;
    @BindView(R.id.iv_left_panel)
    ImageView iv_left_panel;
    @BindView(R.id.tv_left_panel)
    TextView tv_left_panel;
    @BindView(R.id.iv_right_panel)
    ImageView iv_right_panel;
    @BindView(R.id.tv_right_panel)
    TextView tv_right_panel;

    @BindView(R.id.optionFirst)
    TextView optionFirst;
    @BindView(R.id.optionSecond)
    TextView optionSecond;
    @BindView(R.id.optionThird)
    TextView optionThird;
    @BindView(R.id.optionFourth)
    TextView optionFourth;

    private final String TAG = this.getClass().getSimpleName();
    RateFragmentPresenter presenter;
    RateChart chart;
    ChartOption selectedChartOption;

    Coin crypto_selected;
    Coin country_selected;


    public RateFragment() { }

    public static RateFragment newInstance() {
        return new RateFragment();
    }

    @Override
    public void loadData() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public View setupView(View view) {
        Log.i(TAG, "setupView()");

//        swipe_container.setOnRefreshListener(this);
//        swipe_container.setColorSchemeColors(
//                getResources().getColor(R.color.colorAccent),
//                getResources().getColor(R.color.colorChart),
//                getResources().getColor(R.color.colorLabelBackground));

        view.findViewById(R.id.optionFirst).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionSecond).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionThird).setOnClickListener(onChartOptionClick);
        view.findViewById(R.id.optionFourth).setOnClickListener(onChartOptionClick);

        chart = new RateChart(view);
        chart.initialize();

        setCurrenciesSpinner();

        getSelectedChartOption();
        setChartLabelSelected(getSelectedChartLabelView());

        String title = getResources().getString(R.string.title_search_dialog_name);
        String hint = getResources().getString(R.string.title_search_dialog_hint);

        left_panel.setOnClickListener(v -> {
            ArrayList<Coin> arrayList = new ArrayList<>(Arrays.asList(crypto_coins));

            SearchDialog dialogCompat = new SearchDialog(getContext(), title, hint, null, arrayList,
                    new SearchResultListener<Coin>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Coin coin, int i) {
                            tv_left_panel.setText(coin.symbol);
                            Glide.with(RateFragment.this)
                                    .asBitmap()
                                    .load(coin.imageUrl)
                                    .into(iv_left_panel);

                            App.getAppInstance().getPreferences().setLeftSpinnerValue(coin.symbol);
                            crypto_selected = coin;

                            presenter.showRate(false);
                            baseSearchDialogCompat.dismiss();
                        }
                    });
            dialogCompat.show();

        });

        right_panel.setOnClickListener(v -> {
            ArrayList<Coin> arrayList = new ArrayList<>(Arrays.asList(country_coins));

            SearchDialog dialogCompat = new SearchDialog(getContext(), title, hint, null, arrayList,
                    new SearchResultListener<Coin>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Coin coin, int i) {
                            tv_right_panel.setText(coin.symbol);
                            Glide.with(RateFragment.this)
                                    .asBitmap()
                                    .load(Codes.getCountryImage(coin.symbol))
                                    .into(iv_right_panel);

                            App.getAppInstance().getPreferences().setRightSpinnerValue(coin.symbol);
                            country_selected = coin;

                            presenter.showRate(false);
                            baseSearchDialogCompat.dismiss();
                        }
                    });
            dialogCompat.show();
        });

        return view;
    }

    @Override
    public void setCurrenciesSpinner() {

        String left_value = App.getAppInstance().getPreferences().getLeftSpinnerValue();
        String right_value = App.getAppInstance().getPreferences().getRightSpinnerValue();

        crypto_selected = Codes.getCryptoCoinByCode(left_value);
        country_selected = Codes.getCountryCoinByCode(right_value);

        tv_left_panel.setText(left_value);
        tv_right_panel.setText(right_value);

        Glide.with(this)
                .asBitmap()
                .load(crypto_selected.imageUrl)
                .into(iv_left_panel);

        Glide.with(this)
                .asBitmap()
                .load(Codes.getCountryImage(right_value))
                .into(iv_right_panel);

    }

    View.OnClickListener onChartOptionClick = v -> {
        setChartLabelSelected(v);
        getChart(getSelectedChartOption());
    };

    private void getChart(ChartOption chartOption) {
        presenter.showChart(crypto_selected.symbol, country_selected.symbol, chartOption, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_rate, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.refresh();
                presenter.showRate(true);
                Toast.makeText(getContext(), getString(R.string.updating), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        presenter = getPresenter(RateFragmentPresenter.class);
        presenter.setView(this);
        presenter.setRouter((Router) getActivity());
        presenter.onStart();

        super.onStart();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    public void onStop() {
        presenter.saveInstanceState(new RateFragmentInstanceState(selectedChartOption));
        presenter.setView(null);
        presenter.onStop();

        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public TextView getTopPanel() {
        return topPanel;
    }

    //    @Override
//    public void onRefresh() {
//        presenter.refresh();
//    }

    //    @Override
//    public SwipeRefreshLayout getRefreshLayout() {
//        return swipe_container;
//    }

    @Override
    public void showProgressbar() {
        super.showProgressbar();
//        swipe_container.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressbar() {
        super.hideProgressbar();
//        swipe_container.setVisibility(View.VISIBLE);
    }

    @Override
    public RateChart getRateChart() {
        return chart;
    }


    public ChartOption getSelectedChartOption() {
        if (selectedChartOption == null) setSelectedChartOption(chart_options[0]);

        return selectedChartOption;
    }
    public void setSelectedChartOption(ChartOption chartOption) {
        selectedChartOption = chartOption;
    }

    public void setChartLabelSelected(View view) {

        switch (view.getId()) {
            case R.id.optionFirst:
                setSelectedChartOption(chart_options[0]);
                break;
            case R.id.optionSecond:
                setSelectedChartOption(chart_options[1]);
                break;
            case R.id.optionThird:
                setSelectedChartOption(chart_options[2]);
                break;
            case R.id.optionFourth:
                setSelectedChartOption(chart_options[3]);
                break;
        }

        optionFirst.setBackground(getResources().getDrawable(R.drawable.bg_label_unselected));
        optionSecond.setBackground(getResources().getDrawable(R.drawable.bg_label_unselected));
        optionThird.setBackground(getResources().getDrawable(R.drawable.bg_label_unselected));
        optionFourth.setBackground(getResources().getDrawable(R.drawable.bg_label_unselected));

        view.setBackground(getResources().getDrawable(R.drawable.bg_label_selected));
    }

    public TextView getSelectedChartLabelView() {

        if (selectedChartOption.equals(chart_options[0]))
            return optionFirst;
        else
            if (selectedChartOption.equals(chart_options[1]))
            return optionSecond;
        else
            if (selectedChartOption.equals(chart_options[2]))
            return optionThird;
        else
            if (selectedChartOption.equals(chart_options[3]))
            return optionFourth;

        else return null;
    }


    public Coin getCrypto_selected() {
        return crypto_selected;
    }

    public Coin getCountry_selected() {
        return country_selected;
    }
}












