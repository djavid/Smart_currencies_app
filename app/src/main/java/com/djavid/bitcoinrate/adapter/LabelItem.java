package com.djavid.bitcoinrate.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.DataRepository;
import com.djavid.bitcoinrate.model.RestDataRepository;
import com.djavid.bitcoinrate.model.dto.LabelItemDto;
import com.djavid.bitcoinrate.model.realm.LabelItemRealm;
import com.djavid.bitcoinrate.model.realm.TickerItemRealm;
import com.djavid.bitcoinrate.util.RxUtils;
import com.djavid.bitcoinrate.view.activity.MainActivity;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import io.realm.Realm;


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

    Realm realm;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    public LabelItemDto labelItemDto;

    LabelItem(Context mContext, PlaceHolderView mPlaceHolderView, LabelItemDto labelItemDto,
              TickerItem tickerItem) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.labelItemDto = labelItemDto;
        this.tickerItem = tickerItem;

        isAddButton = labelItemDto.isAddButton();
        realm = Realm.getDefaultInstance();
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

    @Click(R.id.ll_label_btn)
    private void onClick() {
        if (isAddButton) {
            ((MainActivity) mContext).showCreateLabelDialog(tickerItem);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Удалить оповещение");
            alert.setMessage("Вы действительно хотите удалить это оповещение?");

            alert.setPositiveButton("Да", (dialog, which) -> {
                deleteSubscribe(labelItemDto.getId());

                realm.executeTransaction(realm1 -> {
                    TickerItemRealm itemRealm = realm1
                            .where(TickerItemRealm.class)
                            .equalTo("createdAt", tickerItem.getCreatedAt()).findFirst();

                    if (itemRealm != null) {
                        for (LabelItemRealm item : itemRealm.getLabels()) {
                            if (item.getId() == labelItemDto.getId()) {
                                item.deleteFromRealm();
                                tickerItem.deleteLabel(this);
                                break;
                            }
                        }
                    }

                });
            });

            alert.setNegativeButton("Нет", ((dialog, which) -> {
                dialog.dismiss();
            }));

            alert.show();
        }
    }

    private void deleteSubscribe(Long id) {
        DataRepository dataRepository = new RestDataRepository();

        dataRepository.deleteSubscribe(id)
                .compose(RxUtils.applyCompletableSchedulers())
                .doOnError(Throwable::printStackTrace)
                .subscribe(() -> {
                    Log.d("LabelDialog", "Succesfully deleted subscribe with id = " + id);
                }, error -> {

                });
    }

}
