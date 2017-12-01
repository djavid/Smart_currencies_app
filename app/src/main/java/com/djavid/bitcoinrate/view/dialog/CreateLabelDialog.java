package com.djavid.bitcoinrate.view.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.dto.heroku.Subscribe;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.model.realm.LabelItemRealm;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.activity.MainActivity;

import butterknife.BindView;


public class CreateLabelDialog extends BaseDialogFragment {

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

    private TickerItem tickerItem;


    public CreateLabelDialog() { }

    public static CreateLabelDialog newInstance() {
        CreateLabelDialog fragment = new CreateLabelDialog();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View setupView(View view) {
        tv_cancel_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        tv_create_btn.setOnClickListener(v -> {
            if (et_price.getText().toString().isEmpty()) {
                //TODO show alert
            } else {

                String value = et_price.getText().toString();
                boolean isTrendingUp;
                isTrendingUp = btn_trending_up.isChecked();

                LabelItemDto labelItemDto = new LabelItemDto(value, isTrendingUp);

                long token_id = App.getAppInstance().getPrefencesWrapper()
                        .sharedPreferences.getLong("token_id", 0);
                TickerItem selectedTicker = ((MainActivity) getActivity()).getSelectedTickerItem();
                long ticker_id = selectedTicker.getTickerItem().getId();
                String cryptoId = selectedTicker.getTickerItem().getCryptoId();
                String countryId = selectedTicker.getTickerItem().getCountryId();

                Subscribe subscribe = new Subscribe(isTrendingUp, value, ticker_id, token_id, cryptoId, countryId);
                sendSubscribe(subscribe, labelItemDto, selectedTicker);

                this.dismiss();
            }
        });

        return view;
    }

    private void sendSubscribe(Subscribe subscribe, LabelItemDto label, TickerItem tickerItem) {
        DataRepository dataRepository = new RestDataRepository();

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
                    }
                }, error -> {

                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_label_dialog;
    }
}