package com.djavid.bitcoinrate.view.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.model.project.LabelItemDto;
import com.djavid.bitcoinrate.rest.RestDataRepository;
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
    LabelItemDto labelItemDto;


    LabelItem(Context mContext, PlaceHolderView mPlaceHolderView, LabelItemDto labelItemDto,
              TickerItem tickerItem) {
        this.mContext = mContext;
        this.mPlaceHolderView = mPlaceHolderView;
        this.labelItemDto = labelItemDto;
        this.tickerItem = tickerItem;

        isAddButton = labelItemDto.isAddButton();
    }


    @Resolve
    private void onResolved() {

        if (!labelItemDto.isAddButton()) {

            if (labelItemDto.isPercentLabel()) {

                String text = labelItemDto.getChange_percent() * 100 + " %";
                tv_ticker_label.setText(text);

                tv_ticker_label.setVisibility(android.view.View.VISIBLE);
                iv_label_trending.setVisibility(android.view.View.GONE);
                iv_label_add.setVisibility(android.view.View.GONE);

            } else {

                tv_ticker_label.setText(labelItemDto.getValue());

                if (labelItemDto.isTrendingUp())
                    iv_label_trending.setImageResource(R.drawable.ic_trending_up_white_24px);
                else
                    iv_label_trending.setImageResource(R.drawable.ic_trending_down_white_24px);

                tv_ticker_label.setVisibility(android.view.View.VISIBLE);
                iv_label_trending.setVisibility(android.view.View.VISIBLE);
                iv_label_add.setVisibility(android.view.View.GONE);
            }

        } else {

            tv_ticker_label.setVisibility(android.view.View.GONE);
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
            });

            alert.setNegativeButton("Нет", ((dialog, which) -> {
                dialog.dismiss();
            }));

            alert.show();
        }
    }

    private void deleteSubscribe(Long id) {

        RestDataRepository dataRepository = new RestDataRepository();

        dataRepository.deleteSubscribe(id)
                .subscribe(() -> {
                    tickerItem.deleteLabel(this);

                    int amount = App.getAppInstance().getPreferences().getSubscribesAmount();
                    App.getAppInstance().getPreferences().setSubscribesAmount(--amount);

                    Log.d("LabelDialog", "Successfully deleted subscribe with id = " + id);
                }, error -> {
                    showError(R.string.error_deleting_subscribe);
                });
    }

    private void showError(int errorId) {
        Toast.makeText(App.getContext(),
                App.getContext().getString(errorId), Toast.LENGTH_SHORT).show();
    }

}
