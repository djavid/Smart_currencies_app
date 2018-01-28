package com.djavid.bitcoinrate.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.heroku.Ticker;
import com.djavid.bitcoinrate.model.project.Coin;
import com.djavid.bitcoinrate.rest.RestDataRepository;
import com.djavid.bitcoinrate.util.Codes;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

import static com.djavid.bitcoinrate.util.Config.country_coins;
import static com.djavid.bitcoinrate.util.Config.crypto_coins;


public class CreateTickerDialog extends BaseDialogFragment {

    @BindView(R.id.tv_cancel_btn)
    TextView tv_cancel_btn;
    @BindView(R.id.tv_create_btn)
    TextView tv_create_btn;

    @BindView(R.id.left_panel_t)
    LinearLayout left_panel;
    @BindView(R.id.right_panel_t)
    LinearLayout right_panel;
    @BindView(R.id.iv_left_panel_t)
    ImageView iv_left_panel;
    @BindView(R.id.tv_left_panel_t)
    TextView tv_left_panel;
    @BindView(R.id.iv_right_panel_t)
    ImageView iv_right_panel;
    @BindView(R.id.tv_right_panel_t)
    TextView tv_right_panel;

    Coin crypto_selected;
    Coin country_selected;


    public CreateTickerDialog() { }

    public static CreateTickerDialog newInstance(ArrayList<String> pairs) {

        CreateTickerDialog fragment = new CreateTickerDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("pairs", pairs);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View setupView(View view) {

        tv_cancel_btn.setOnClickListener(v -> {
            this.dismiss();
        });

        tv_create_btn.setOnClickListener(v -> {

            try {
                ArrayList<String> pairs = getArguments().getStringArrayList("pairs");

                String crypto_symbol = crypto_selected.symbol;
                String country_symbol = country_selected.symbol;
                String pair = crypto_symbol + country_symbol;

                if (pairs != null) {
                    if (pairs.contains(pair)) {
                        showError(R.string.error_pair_already_added);
                    } else {
                        long token_id = App.getAppInstance().getPreferences().getTokenId();
                        Ticker ticker = new Ticker(token_id, crypto_symbol, country_symbol);

                        sendTicker(ticker);
                        dismiss();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                dismiss();
            }

        });

        String title = getResources().getString(R.string.title_search_dialog_name);
        String hint = getResources().getString(R.string.title_search_dialog_hint);

        left_panel.setOnClickListener(v -> {
            ArrayList<Coin> arrayList = new ArrayList<>(Arrays.asList(crypto_coins));

            SearchDialog dialogCompat = new SearchDialog(getContext(), title, hint, null, arrayList,
                    new SearchResultListener<Coin>() {
                        @Override
                        public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Coin coin, int i) {
                            crypto_selected = coin;
                            tv_left_panel.setText(coin.symbol);
                            Glide.with(CreateTickerDialog.this)
                                    .asBitmap()
                                    .load(coin.imageUrl)
                                    .into(iv_left_panel);
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
                            country_selected = coin;
                            tv_right_panel.setText(coin.symbol);
                            Glide.with(CreateTickerDialog.this)
                                    .asBitmap()
                                    .load(Codes.getCountryImage(coin.symbol))
                                    .into(iv_right_panel);
                            baseSearchDialogCompat.dismiss();
                        }
                    });
            dialogCompat.show();
        });

        setCurrenciesSpinner();

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_ticker_dialog;
    }


    private void sendTicker(Ticker ticker) {

        RestDataRepository dataRepository = new RestDataRepository();

        dataRepository.sendTicker(ticker)
                .subscribe(response -> {

                    if (response.error.isEmpty()) {
                        Log.d("TickerDialog", "Succesfully sent " + ticker.toString());

                        if (response.id != 0) {

                            Bundle bundle = new Bundle();
                            bundle.putString("cryptoId", ticker.getCryptoId());
                            bundle.putString("countryId", ticker.getCountryId());
                            bundle.putLong("id", response.id);

                            Intent intent = new Intent().putExtras(bundle);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                        }

                    } else {
                        Log.e("TickerDialog", response.error);
                        showError(R.string.connection_error);
                    }

                }, error -> {
                    showError(R.string.connection_error);
                });
    }

    public void setCurrenciesSpinner() {

        crypto_selected = crypto_coins[0];
        country_selected = country_coins[0];

        tv_left_panel.setText(crypto_selected.symbol);
        tv_right_panel.setText(country_selected.symbol);

        Glide.with(this)
                .asBitmap()
                .load(crypto_selected.imageUrl)
                .into(iv_left_panel);

        Glide.with(this)
                .asBitmap()
                .load(Codes.getCountryImage(country_selected.symbol))
                .into(iv_right_panel);
    }

}
