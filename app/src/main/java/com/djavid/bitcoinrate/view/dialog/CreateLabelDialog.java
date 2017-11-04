package com.djavid.bitcoinrate.view.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.adapter.TickerItem;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.model.realm.LabelItemRealm;
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

            } else {

                String value = et_price.getText().toString();
                boolean isTrendingUp;
                isTrendingUp = btn_trending_up.isChecked();

                LabelItemRealm labelItemRealm = new LabelItemRealm(value, isTrendingUp);
                ((MainActivity) getActivity()).getSelectedTickerItem().addLabelItem(labelItemRealm);

                this.dismiss();
            }
        });

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_create_label_dialog;
    }
}
