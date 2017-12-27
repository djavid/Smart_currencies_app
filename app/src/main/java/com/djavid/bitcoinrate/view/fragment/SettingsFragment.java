package com.djavid.bitcoinrate.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.annimon.stream.IntStream;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.view.adapter.CurrenciesAdapter;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.djavid.bitcoinrate.util.Codes.country_coins;
import static com.djavid.bitcoinrate.util.Codes.crypto_coins_array_code;


public class SettingsFragment extends Fragment {

    @BindView(R.id.leftSpinner)
    Spinner leftSpinner;
    @BindView(R.id.rightSpinner)
    Spinner rightSpinner;

    @BindView(R.id.btn_hour)
    RadioButton btn_hour;
    @BindView(R.id.btn_day)
    RadioButton btn_day;
    @BindView(R.id.btn_week)
    RadioButton btn_week;

    @BindView(R.id.btn_codes)
    RadioButton btn_codes;
    @BindView(R.id.btn_titles)
    RadioButton btn_titles;

    @BindView(R.id.btn_about_developer)
    Button btn_about_developer;
    @BindView(R.id.ll_parent)
    LinearLayout ll_parent;


    private Unbinder unbinder;
    private final String TAG = this.getClass().getSimpleName();


    public SettingsFragment() {}

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

        setCurrenciesSpinner();

        btn_hour.setOnClickListener(percentOnClickListener);
        btn_day.setOnClickListener(percentOnClickListener);
        btn_week.setOnClickListener(percentOnClickListener);

        btn_titles.setOnClickListener(titleFormatOnClickListener);
        btn_codes.setOnClickListener(titleFormatOnClickListener);

        btn_about_developer.setOnClickListener(aboutBtnOnClickListener);

        setCheckedRadioButton();

        return view;
    }

    private void setCheckedRadioButton() {

        switch (App.getAppInstance().getSavedPercentChange()) {
            case "hour":
                btn_hour.setChecked(true);
                break;
            case "day":
                btn_day.setChecked(true);
                break;
            case "week":
                btn_week.setChecked(true);
                break;
        }

        switch (App.getAppInstance().getSavedTitleFormat()) {
            case "codes":
                btn_codes.setChecked(true);
                break;
            case "titles":
                btn_titles.setChecked(true);
                break;
        }
    }

    View.OnClickListener aboutBtnOnClickListener = v -> {

        EasyPopup popup_window = new EasyPopup(getContext())
                .setContentView(R.layout.about_developer)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.3f)
                .createPopup();

        popup_window.showAtAnchorView(ll_parent, VerticalGravity.CENTER, HorizontalGravity.CENTER);

    };

    View.OnClickListener percentOnClickListener = v -> {

        switch (v.getId()) {
            case R.id.btn_hour:
                App.getAppInstance().getSharedPreferences()
                        .edit()
                        .putString("display_price_change", "hour")
                        .apply();
                break;
            case R.id.btn_day:
                App.getAppInstance().getSharedPreferences()
                        .edit()
                        .putString("display_price_change", "day")
                        .apply();
                break;
            case R.id.btn_week:
                App.getAppInstance().getSharedPreferences()
                        .edit()
                        .putString("display_price_change", "week")
                        .apply();
                break;
        }
    };

    View.OnClickListener titleFormatOnClickListener = v -> {

        switch (v.getId()) {
            case R.id.btn_codes:
                App.getAppInstance().getSharedPreferences()
                        .edit()
                        .putString("title_format", "codes")
                        .apply();
                break;
            case R.id.btn_titles:
                App.getAppInstance().getSharedPreferences()
                        .edit()
                        .putString("title_format", "titles")
                        .apply();
                break;
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void showError(int errorId) {
        Toast.makeText(getContext(), getString(errorId), Toast.LENGTH_SHORT).show();
    }

    public void setCurrenciesSpinner() {

        String left_default = App.getAppInstance().getSharedPreferences()
                .getString("left_spinner_value", "BTC");
        String right_default = App.getAppInstance().getSharedPreferences()
                .getString("right_spinner_value", "USD");

        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row,
                crypto_coins_array_code, getActivity().getLayoutInflater(), R.layout.ticker_row_item);
        leftSpinner.setAdapter(adapterLeft);

        int id_left = IntStream.range(0, crypto_coins_array_code.length)
                .filter(i -> crypto_coins_array_code[i].equals(left_default)).findFirst().getAsInt();
        leftSpinner.setSelection(id_left, false);
        leftSpinner.setOnItemSelectedListener(itemSelectedListener);

        ArrayAdapter<String> adapterRight = new CurrenciesAdapter(getActivity(), R.layout.row, country_coins,
                getActivity().getLayoutInflater(), R.layout.ticker_row_item);
        rightSpinner.setAdapter(adapterRight);

        int id_right = IntStream.range(0, country_coins.length)
                .filter(i -> country_coins[i].equals(right_default)).findFirst().getAsInt();
        rightSpinner.setSelection(id_right);
        rightSpinner.setOnItemSelectedListener(itemSelectedListener);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i(TAG, "Selected spinner item '" + parent.getSelectedItem() + "'");

                    if (parent.getId() == R.id.leftSpinner) {
                        App.getAppInstance().getSharedPreferences()
                                .edit()
                                .putString("left_spinner_value", parent.getSelectedItem().toString())
                                .apply();
                    }

                    if (parent.getId() == R.id.rightSpinner) {
                        App.getAppInstance().getSharedPreferences()
                                .edit()
                                .putString("right_spinner_value", parent.getSelectedItem().toString())
                                .apply();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

}
