package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.realm.LabelItemRealm;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.util.Codes;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Position;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;

import java.io.Serializable;



@NonReusable
@Layout(R.layout.recycler_item_ticker)
public class TickerItem implements Serializable {

    @View(R.id.tv_ticker_title)
    private TextView tv_ticker_title;
    @View(R.id.iv_ticker_icon)
    private ImageView iv_ticker_icon;
    @View(R.id.tickerValue)
    private TextView tickerValue;
    @View(R.id.label_container)
    private PlaceHolderView label_container;


    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private TickerItemRealm itemRealm;
    private String tv_price;

    public TickerItem(Context mContext, PlaceHolderView mPlaceHolderView, TickerItemRealm itemRealm) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.itemRealm = itemRealm;
    }

    @Resolve
    private void onResolved() {
        tv_ticker_title.setText(Codes.getCurrencyFullName(itemRealm.getCode_crypto()).split(";")[1]);
        iv_ticker_icon.setImageResource(Codes.getCurrencyImage(itemRealm.getCode_crypto()));
        tickerValue.setText(tv_price);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        label_container.setLayoutManager(layoutManager);

        for (LabelItemRealm item : itemRealm.getLabels()) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemRealm(), this));
    }

    public void addLabelItem(LabelItemRealm new_item) {
        label_container.removeAllViews();

        itemRealm.getLabels().add(itemRealm.getLabels().size(), new_item);

        for (LabelItemRealm item : itemRealm.getLabels()) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemRealm(), this));
    }

    public TickerItemRealm getRealm() {
        return itemRealm;
    }

    public void setPrice(String price) {
        tv_price = price + " " + itemRealm.getCode_country();
        tickerValue.setText(tv_price);
    }

}
