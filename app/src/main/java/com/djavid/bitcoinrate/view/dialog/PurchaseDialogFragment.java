package com.djavid.bitcoinrate.view.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.core.BaseDialogFragment;
import com.djavid.bitcoinrate.view.activity.MainActivity;

import butterknife.BindView;


public class PurchaseDialogFragment extends BaseDialogFragment {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_purchase_info_1)
    TextView tv_purchase_info_1;
    @BindView(R.id.tv_purchase_info_2)
    TextView tv_purchase_info_2;
    @BindView(R.id.btn_purchase)
    Button btn_purchase;


    public PurchaseDialogFragment() { }

    public static PurchaseDialogFragment newInstance() {
        PurchaseDialogFragment fragment = new PurchaseDialogFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_purchase_dialog;
    }

    @Override
    public View setupView(View view) {

        tv_purchase_info_1.setText(getText(R.string.purchase_info_1));
        tv_purchase_info_2.setText(getText(R.string.purchase_info_2));

        btn_purchase.setOnClickListener(v -> {

            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).purchase();
            }

        });

        return view;
    }
}
