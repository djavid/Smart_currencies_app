package com.djavid.bitcoinrate.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class SettingsFragment extends Fragment {

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

        switch (App.getAppInstance().getPreferences().getShowedPriceChange()) {
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

        switch (App.getAppInstance().getPreferences().getTitleFormat()) {
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
                App.getAppInstance().getPreferences().setShowedPriceChange("hour");
                break;

            case R.id.btn_day:
                App.getAppInstance().getPreferences().setShowedPriceChange("day");
                break;

            case R.id.btn_week:
                App.getAppInstance().getPreferences().setShowedPriceChange("week");
                break;
        }
    };

    View.OnClickListener titleFormatOnClickListener = v -> {

        switch (v.getId()) {

            case R.id.btn_codes:
                App.getAppInstance().getPreferences().setTitleFormat("codes");
                break;

            case R.id.btn_titles:
                App.getAppInstance().getPreferences().setTitleFormat("titles");
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

}
