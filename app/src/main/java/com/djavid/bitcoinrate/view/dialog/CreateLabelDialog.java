package com.djavid.bitcoinrate.view.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.activity.MainActivity;
import com.djavid.bitcoinrate.view.adapter.TickerItem;
import com.tomergoldst.tooltips.ToolTip;
import com.tomergoldst.tooltips.ToolTipsManager;

import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;


public class CreateLabelDialog extends BaseDialogFragment {

    @BindView(R.id.segmented_button)
    SegmentedGroup segmented_button;
    @BindView(R.id.btn_trending_up)
    RadioButton btn_trending_up;
    @BindView(R.id.btn_trending_down)
    RadioButton btn_trending_down;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.tv_cancel_btn)
    TextView tv_cancel_btn;
    @BindView(R.id.tv_create_btn)
    TextView tv_create_btn;
    @BindView(R.id.cb_percent_change)
    CheckBox cb_percent_change;
    @BindView(R.id.tv_price_value)
    TextView tv_price;

    @BindView(R.id.tv_change)
    TextView tv_change;
    @BindView(R.id.tv_change_value)
    TextView tv_change_value;
    @BindView(R.id.iv_help)
    ImageView iv_help;
    @BindView(R.id.rl_label_dialog)
    RelativeLayout rl_label_dialog;

    private ToolTipsManager mToolTipsManager;
    private View tooltipView;


    public CreateLabelDialog() { }

    public static CreateLabelDialog newInstance() {

        CreateLabelDialog fragment = new CreateLabelDialog();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolTipsManager = new ToolTipsManager();
    }

    @Override
    public View setupView(View view) {

        try {
            TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();

            String price = DateFormatter.convertPrice(
                    selectedTicker.getTickerItem().getTicker().getPrice()) + " "
                    + selectedTicker.getTickerItem().getCountryId();
            tv_price.setText(price);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        tv_change.setVisibility(View.GONE);
        tv_change_value.setVisibility(View.GONE);

        tv_cancel_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        tv_create_btn.setOnClickListener(addBtnOnCLickListener);

        cb_percent_change.setOnCheckedChangeListener(percentOnCheckedChangeListener);

        iv_help.setOnClickListener(helpBtnOnClickListener);

        et_price.addTextChangedListener(textWatcherPercent);

        return view;
    }

    View.OnClickListener addBtnOnCLickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();

                String value = et_price.getText().toString();
                boolean isTrendingUp = btn_trending_up.isChecked();
                boolean isPercentLabel = cb_percent_change.isChecked();

                if (!isValidValue(value, isPercentLabel)) {
                    return;
                }

                if (selectedTicker == null || selectedTicker.getTickerItem() == null) {
                    showError(R.string.error_occurred);
                    dismiss();
                    return;
                }

                long token_id = App.getAppInstance().getPreferences().getTokenId();
                long ticker_id = selectedTicker.getTickerItem().getId();
                String cryptoId = selectedTicker.getTickerItem().getCryptoId();
                String countryId = selectedTicker.getTickerItem().getCountryId();

                LabelItemDto labelItemDto;
                Subscribe subscribe;

                if (isPercentLabel) {

                    String perc = Double.toString(Double.parseDouble(value) / 100);
                    subscribe = new Subscribe(perc, ticker_id, token_id, cryptoId, countryId,
                            selectedTicker.getTickerItem().getTicker().getPrice());
                    labelItemDto = new LabelItemDto(perc, isTrendingUp, true);

                } else {
                    subscribe = new Subscribe(isTrendingUp, value, ticker_id, token_id, cryptoId, countryId);
                    labelItemDto = new LabelItemDto(value, isTrendingUp, false);
                }

                sendSubscribe(subscribe, labelItemDto, selectedTicker);
                dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                showError(R.string.error_occurred);
            }

        }
    };

    CompoundButton.OnCheckedChangeListener percentOnCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (isChecked) {

                tv_change.setVisibility(View.VISIBLE);
                tv_change_value.setVisibility(View.VISIBLE);
                tv_change_value.setText("±0");

                segmented_button.setVisibility(View.GONE);
                et_price.setHint(R.string.title_hint_percent);

            } else {

                tv_change.setVisibility(View.GONE);
                tv_change_value.setVisibility(View.GONE);

                segmented_button.setVisibility(View.VISIBLE);
                et_price.setHint(R.string.title_hint_price);
            }

        }
    };

    View.OnClickListener helpBtnOnClickListener = v -> {

        if (tooltipView != null && mToolTipsManager.isVisible(tooltipView)) {

            mToolTipsManager.dismiss(tooltipView, true);

        } else {

            if (getContext() != null) {

                String hint;
                if (cb_percent_change.isChecked())
                    hint = getString(R.string.hint_percent);
                else
                    hint = getString(R.string.hint_fixed_price);

                ToolTip.Builder builder = new ToolTip.Builder(getContext(), iv_help,
                        rl_label_dialog, hint, ToolTip.POSITION_BELOW);

                builder.setBackgroundColor(getResources().getColor(R.color.colorSettingsAccent));

                tooltipView = mToolTipsManager.show(builder.build());
            }
        }

    };

    TextWatcher textWatcherPercent = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            mToolTipsManager.dismissAll();

            if (cb_percent_change.isChecked()) {

                if (s.toString().isEmpty()) {
                    tv_change_value.setText("±0 ");
                    return;
                }

                try {
                    TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();

                    double price = selectedTicker.getTickerItem().getTicker().getPrice();
                    double change = price * (Double.parseDouble(s.toString()) / 100.0);

                    String text = "±" + DateFormatter.convertPrice(change) + " " +
                            Codes.getCountrySymbol(selectedTicker.getTickerItem().getCountryId());
                    tv_change_value.setText(text);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    };

    private boolean isValidValue(String value, boolean isPercentLabel) {

        if (value.isEmpty()) {
            showErrorHint(R.string.error_empty_value);
            return false;
        }

        if (value.startsWith(".") || value.endsWith(".")) {
            showErrorHint(R.string.error_wrong_format);
            return false;
        }

        if (value.contains("-") || value.contains(" ")) {
            showErrorHint(R.string.error_wrong_format);
            return false;
        }

        if (isPercentLabel) {
            try {
                double perc = Double.parseDouble(value);

                if (perc > 200 || perc <= 0) {
                    showErrorHint(R.string.error_percent_max);
                    return false;
                }

                if (value.contains(".")) {
                    if (value.split("\\.")[1].length() > 4) {
                        showErrorHint(R.string.error_percent_double);
                        return false;
                    }
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void sendSubscribe(Subscribe subscribe, LabelItemDto label, TickerItem tickerItem) {

        RestDataRepository dataRepository = new RestDataRepository();

        System.out.println(subscribe);
        dataRepository.sendSubscribe(subscribe)
                .compose(RxUtils.applySingleSchedulers())
                .subscribe(response -> {

                    if (response.error.isEmpty()) {
                        Log.d("LabelDialog", "Successfully sent " + subscribe.toString());

                        if (response.id != 0) {
                            label.setId(response.id);
                            tickerItem.addLabelItem(label);
                        }
                    } else {
                        Log.e("LabelDialog", response.error);
                        //todo don't add similar labels, fix on server and sqoh it hear as error
                        showError(R.string.connection_error);
                    }

                }, error -> {
                    showError(R.string.connection_error);
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_label_dialog;
    }

    private void showErrorHint(int res) {

        String hint = getString(res);

        ToolTip.Builder builder = new ToolTip.Builder(getContext(), et_price,
                rl_label_dialog, hint, ToolTip.POSITION_BELOW);
        builder.setBackgroundColor(getResources().getColor(R.color.colorPriceChangeNeg));

        mToolTipsManager.dismissAll();
        tooltipView = mToolTipsManager.show(builder.build());
    }

}