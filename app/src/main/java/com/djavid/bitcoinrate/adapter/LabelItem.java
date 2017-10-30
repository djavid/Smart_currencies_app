package com.djavid.bitcoinrate.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;


@NonReusable
@Layout(R.layout.ticker_label_item)
public class LabelItem {

    @View(R.id.tv_ticker_label)
    private TextView tv_ticker_label;
    @View(R.id.iv_label_trending)
    private ImageView iv_label_trending;
    @View(R.id.iv_label_add)
    private ImageView iv_label_add;


    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    private LabelItemDto labelItemDto;

    public LabelItem(Context mContext, PlaceHolderView mPlaceHolderView, LabelItemDto labelItemDto) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.labelItemDto = labelItemDto;
    }

    @Resolve
    private void onResolved() {
        if (!labelItemDto.isAddButton()) {
            tv_ticker_label.setText(labelItemDto.getValue());

            if (labelItemDto.isTrendingUp())
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

}
