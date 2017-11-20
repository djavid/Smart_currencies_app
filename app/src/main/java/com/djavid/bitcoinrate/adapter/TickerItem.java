package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.realm.Realm;


@NonReusable
@Layout(R.layout.recycler_item_ticker)
public class TickerItem {

    @View(R.id.tv_ticker_title)
    private TextView tv_ticker_title;
    @View(R.id.iv_ticker_icon)
    private ImageView iv_ticker_icon;
    @View(R.id.tickerValue)
    private TextView tickerValue;
    @View(R.id.label_container)
    private PlaceHolderView label_container;

    private Date createdAt;
    private String code_crypto;
    private String code_country;
    private List<LabelItemDto> labels;

    Realm realm;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private String tv_price;
    Subject<String> mObservable = PublishSubject.create();


    public TickerItem(Context mContext, PlaceHolderView mPlaceHolderView, TickerItemRealm itemRealm) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;

        realm = Realm.getDefaultInstance();

        createdAt = itemRealm.getCreatedAt();
        code_crypto = itemRealm.getCode_crypto();
        code_country = itemRealm.getCode_country();
        labels = new ArrayList<>();
        for (LabelItemRealm item : itemRealm.getLabels()) {
            labels.add(new LabelItemDto(item.getId(), item.getValue(),
                    item.isTrendingUp(), item.isAddButton()));
        }
    }

    @Resolve
    private void onResolved() {
        tv_ticker_title.setText(Codes.getCurrencyFullName(getCode_crypto()).split(";")[1]);
        iv_ticker_icon.setImageResource(Codes.getCurrencyImage(getCode_crypto()));
        tickerValue.setText(tv_price);


        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        label_container.setLayoutManager(layoutManager);

        for (LabelItemDto item : getLabels()) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemDto(), this));
    }

    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCode_crypto() {
        return code_crypto;
    }
    public void setCode_crypto(String code_crypto) {
        this.code_crypto = code_crypto;
    }

    public String getCode_country() {
        return code_country;
    }
    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    public List<LabelItemDto> getLabels() {
        return labels;
    }
    public void setLabels(List<LabelItemDto> labels) {
        this.labels = labels;
    }

    public void addLabelItem(LabelItemRealm new_item) {
        label_container.removeAllViews();

        labels.add(new LabelItemDto(new_item.getId(), new_item.getValue(),
                new_item.isTrendingUp(), new_item.isAddButton()));

        for (LabelItemDto item : labels) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemDto(), this));

        realm.executeTransaction(realm1 -> {
            TickerItemRealm itemRealm = realm1
                    .where(TickerItemRealm.class)
                    .equalTo("createdAt", createdAt).findFirst();
            if (itemRealm != null) itemRealm.getLabels().add(new_item);

        });
    }

    public void refreshLabels() {
        label_container.removeAllViews();

        for (LabelItemDto item : labels) {
            label_container.addView(new LabelItem(mContext, label_container, item, this));
        }
        label_container.addView(new LabelItem(mContext, label_container, new LabelItemDto(), this));
    }

    void deleteLabel(LabelItem labelItem) {
        labels.remove(labelItem.labelItemDto);
        label_container.removeView(labelItem);
    }

    public void setPrice(String price) {
        tv_price = price + " " + getCode_country();
        if (tickerValue != null) {
            tickerValue.setText(tv_price);
        }
    }




}
