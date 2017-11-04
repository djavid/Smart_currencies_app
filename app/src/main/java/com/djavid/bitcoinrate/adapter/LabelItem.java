package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.realm.LabelItemRealm;
import com.djavid.bitcoinrate.view.activity.MainActivity;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;


@NonReusable
@Layout(R.layout.ticker_label_item)
class LabelItem {

    @View(R.id.tv_ticker_label)
    private TextView tv_ticker_label;
    @View(R.id.iv_label_trending)
    private ImageView iv_label_trending;
    @View(R.id.iv_label_add)
    private ImageView iv_label_add;
    @View(R.id.ll_label_btn)
    private LinearLayout ll_label_btn;

    private Boolean isAddButton;
    private TickerItem tickerItem;


    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private LabelItemRealm labelItemRealm;

    LabelItem(Context mContext, PlaceHolderView mPlaceHolderView, LabelItemRealm labelItemRealm,
              TickerItem tickerItem) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.labelItemRealm = labelItemRealm;
        this.tickerItem = tickerItem;

        isAddButton = labelItemRealm.isAddButton();
    }

    @Resolve
    private void onResolved() {
        if (!labelItemRealm.isAddButton()) {
            tv_ticker_label.setText(labelItemRealm.getValue());

            if (labelItemRealm.isTrendingUp())
                iv_label_trending.setImageResource(R.drawable.ic_trending_up_white_24px);
            else
                iv_label_trending.setImageResource(R.drawable.ic_trending_down_white_24px);

            tv_ticker_label.setVisibility(android.view.View.VISIBLE);
            iv_label_trending.setVisibility(android.view.View.VISIBLE);
            iv_label_add.setVisibility(android.view.View.GONE);
        } else {
            tv_ticker_label.setVisibility(android.view.View.VISIBLE);
            tv_ticker_label.setText("");
            iv_label_trending.setVisibility(android.view.View.GONE);
            iv_label_add.setVisibility(android.view.View.VISIBLE);
        }

    }

    @Click(R.id.ll_label_btn)
    private void onClick() {
        if (isAddButton) {
            ((MainActivity) mContext).showCreateLabelDialog(tickerItem);
        }
    }

}
