package com.djavid.bitcoinrate.view.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.util.DateFormatter;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.activity.MainActivity;
import com.djavid.bitcoinrate.view.adapter.TickerItem;

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
    @BindView(R.id.tv_price)
    TextView tv_price;


    public CreateLabelDialog() { }

    public static CreateLabelDialog newInstance() {
        CreateLabelDialog fragment = new CreateLabelDialog();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View setupView(View view) {

        TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();

        try {

            String price = DateFormatter.convertPrice(selectedTicker.getTickerItem()
                    .getTicker().getPrice()) + " " + selectedTicker.getTickerItem().getCountryId();
            tv_price.setText(price);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        tv_cancel_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        tv_create_btn.setOnClickListener(v -> {

            try {
                String value = et_price.getText().toString();
                boolean isTrendingUp = btn_trending_up.isChecked();
                boolean isPercentLabel = cb_percent_change.isChecked();

                if (isValidValue(value, isPercentLabel, isTrendingUp)) {

                    long token_id = App.getAppInstance().getPreferences().getTokenId();
                    //TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();

                    if (selectedTicker != null && selectedTicker.getTickerItem() != null) {
                        long ticker_id = selectedTicker.getTickerItem().getId();
                        String cryptoId = selectedTicker.getTickerItem().getCryptoId();
                        String countryId = selectedTicker.getTickerItem().getCountryId();

                        LabelItemDto labelItemDto;
                        Subscribe subscribe;

                        if (isPercentLabel) {
                            try {
                                String perc = Double.toString(Double.parseDouble(value) / 100);
                                subscribe = new Subscribe(perc, ticker_id, token_id, cryptoId, countryId,
                                        selectedTicker.getTickerItem().getTicker().getPrice());
                                labelItemDto = new LabelItemDto(perc, isTrendingUp, true);

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return;
                            }

                        } else {
                            subscribe = new Subscribe(isTrendingUp, value, ticker_id, token_id, cryptoId, countryId);
                            labelItemDto = new LabelItemDto(value, isTrendingUp, false);
                        }

                        sendSubscribe(subscribe, labelItemDto, selectedTicker);

                    } else {
                        showError(R.string.error_loading_ticker_try_again);
                        dismiss();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        cb_percent_change.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                segmented_button.setVisibility(View.GONE);
                et_price.setHint(R.string.title_hint_percent);
            } else {
                segmented_button.setVisibility(View.VISIBLE);
                et_price.setHint(R.string.title_hint_price);
            }
        });

        return view;
    }

    private boolean isValidValue(String value, boolean isPercentLabel, boolean isTrendingUp) {

        if (value.isEmpty()) {
            showError(R.string.empty_value);
            return false;
        }
        if (value.startsWith(".") || value.endsWith(".")) return false;
        if (value.contains("-") || value.contains(" ")) return false;

        if (isPercentLabel) {
            try {
                double perc = Double.parseDouble(value);

                if (perc > 200 || perc <= 0) return false;
                if (value.contains(".")) {
                    if (value.split("\\.")[1].length() > 3) return false;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    private void sendSubscribe(Subscribe subscribe, LabelItemDto label, TickerItem tickerItem) {
        DataRepository dataRepository = new RestDataRepository();

        System.out.println(subscribe);
        dataRepository.sendSubscribe(subscribe)
                .compose(RxUtils.applySingleSchedulers())
                .subscribe(response -> {

                    if (response.error.isEmpty()) {
                        Log.d("LabelDialog", "Successfully sent " + subscribe.toString());

                        if (response.id != 0) {
                            label.setId(response.id);
                            tickerItem.addLabelItem(label);

                            this.dismiss();
                        }
                    } else {
                        Log.e("LabelDialog", response.error);
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
}