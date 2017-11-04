package com.djavid.bitcoinrate.view.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.CurrenciesAdapter;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;

import butterknife.BindView;

import static com.djavid.bitcoinrate.util.Codes.country_coins;
import static com.djavid.bitcoinrate.util.Codes.crypto_coins;


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
            //create realm object in local database
            String code_crypto = leftSpinner.getSelectedItem().toString();
            String code_country = rightSpinner.getSelectedItem().toString();

            realm.executeTransactionAsync(realm -> {
                TickerItemRealm tickerItemRealm = realm.createObject(TickerItemRealm.class);
                tickerItemRealm.setCode_crypto(code_crypto);
                tickerItemRealm.setCode_country(code_country);
            }, () -> {
                System.out.println("Created realm object {code_crypto = '" + code_crypto +
                        "', code_country = '" + code_country + "'}");
                this.dismiss();
            });
        });

        setCurrenciesSpinner();

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_ticker_dialog;
    }

    public void setCurrenciesSpinner() {
        ArrayAdapter<String> adapterLeft = new CurrenciesAdapter(getActivity(), R.layout.row, crypto_coins,
                getActivity().getLayoutInflater(), R.layout.ticker_row_item);
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
