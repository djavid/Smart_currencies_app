package com.djavid.bitcoinrate.view.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.heroku.Ticker;
import com.djavid.bitcoinrate.util.Codes;
import com.djavid.bitcoinrate.view.adapter.CurrenciesAdapter;

import butterknife.BindView;

import static com.djavid.bitcoinrate.util.Codes.country_coins;


public class CreateTickerDialog extends BaseDialogFragment {

    @BindView(R.id.tv_cancel_btn)
    TextView tv_cancel_btn;
    @BindView(R.id.tv_create_btn)
    TextView tv_create_btn;
    @BindView(R.id.leftSpinner)
    Spinner leftSpinner;
    @BindView(R.id.rightSpinner)
    Spinner rightSpinner;


    public CreateTickerDialog() { }

    public static CreateTickerDialog newInstance() {
        CreateTickerDialog fragment = new CreateTickerDialog();
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
            String code_crypto = Codes.getCryptoCurrencySymbol(leftSpinner.getSelectedItem().toString());
            String code_country = rightSpinner.getSelectedItem().toString();
            long token_id = App.getAppInstance().getSharedPreferences().getLong("token_id", 0);

            Ticker ticker = new Ticker(token_id, code_crypto, code_country);
            sendTicker(ticker);
        });

        setCurrenciesSpinner();

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_ticker_dialog;
    }


    private void sendTicker(Ticker ticker) {
        DataRepository dataRepository = new RestDataRepository();

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

                            dismiss();
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

        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row,
                Codes.getCryptoCoinsArrayFormatted(), getActivity().getLayoutInflater(), R.layout.ticker_row_item);
        leftSpinner.setAdapter(adapterLeft);
        leftSpinner.setOnItemSelectedListener(itemSelectedListener);

        ArrayAdapter<String> adapterRight = new CurrenciesAdapter(getActivity(), R.layout.row, country_coins,
                getActivity().getLayoutInflater(), R.layout.ticker_row_item);
        rightSpinner.setAdapter(adapterRight);
        rightSpinner.setOnItemSelectedListener(itemSelectedListener);

        leftSpinner.setSelection(0);
        rightSpinner.setSelection(0);
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

}
