package com.djavid.bitcoinrate.view.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.djavid.bitcoinrate.App;
import com.djavid.bitcoinrate.R;
import com.djavid.bitcoinrate.view.fragment.TickerFragment;
import com.zyyoona7.lib.EasyPopup;
import com.zyyoona7.lib.HorizontalGravity;
import com.zyyoona7.lib.VerticalGravity;

import info.hoang8f.android.segmented.SegmentedGroup;


public class TickerPopupWindow {

    private String sorting_parameter = "";
    private String sorting_direction = "";

    private EasyPopup popup_window;
    private Context context;
    private TickerFragment tickerFragment;
    private View menuItemView;


    public TickerPopupWindow(View menuItemView, Context context, TickerFragment tickerFragment) {

        this.context = context;
        this.tickerFragment = tickerFragment;
        this.menuItemView = menuItemView;

        popup_window = new EasyPopup(context)
                .setContentView(R.layout.popup_layout)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.3f)
                .createPopup();

        initListeners();
        setDefaultPopupState();

    }

    public void show() {
        if (menuItemView != null) {
            popup_window.showAtAnchorView(menuItemView, VerticalGravity.BELOW,
                    HorizontalGravity.ALIGN_RIGHT);
        }
    }

    private void initListeners() {
        popup_window.getView(R.id.rbtn_title).setOnClickListener(rbtnTitleOnClickListener);
        popup_window.getView(R.id.rbtn_price).setOnClickListener(rbtnPriceOnClickListener);
        popup_window.getView(R.id.rbtn_market_cap).setOnClickListener(rbtnMarketCapOnClickListener);
        popup_window.getView(R.id.rbtn_growth_percent).setOnClickListener(rbtnGrowthPercentOnClickListener);

        popup_window.getView(R.id.btn_hour).setOnClickListener(btnHourOnClickListener);
        popup_window.getView(R.id.btn_day).setOnClickListener(btnDayOnClickListener);
        popup_window.getView(R.id.btn_week).setOnClickListener(btnWeekOnClickListener);

        popup_window.getView(R.id.imagebutton_up).setOnClickListener(btnUpOnClickListener);
        popup_window.getView(R.id.imagebutton_down).setOnClickListener(btnDownOnClickListener);

        popup_window.getView(R.id.btn_ok).setOnClickListener(btnOkOnClickListener);
    }

    private void setDefaultPopupState() {

        String direction = App.getAppInstance().getPreferences().getSortingDirection();
        String parameter = App.getAppInstance().getPreferences().getSortingParameter();

        switch (parameter) {
            case "title":
                popup_window.getView(R.id.rbtn_title).performClick();
                break;
            case "price":
                popup_window.getView(R.id.rbtn_price).performClick();
                break;
            case "market_cap":
                popup_window.getView(R.id.rbtn_market_cap).performClick();
                break;

            case "hour":
                popup_window.getView(R.id.btn_hour).performClick();
                break;
            case "day":
                popup_window.getView(R.id.btn_day).performClick();
                break;
            case "week":
                popup_window.getView(R.id.btn_week).performClick();
                break;
        }

        switch (direction) {
            case "ascending":
                popup_window.getView(R.id.imagebutton_up).performClick();
                break;

            case "descending":
                popup_window.getView(R.id.imagebutton_down).performClick();
                break;
        }
    }


    private View.OnClickListener rbtnTitleOnClickListener = v -> {
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupBtnUnselected));
        sorting_parameter = "title";
    };

    private View.OnClickListener rbtnPriceOnClickListener = v -> {
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupBtnUnselected));
        sorting_parameter = "price";
    };

    private View.OnClickListener rbtnMarketCapOnClickListener = v -> {
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupBtnUnselected));
        sorting_parameter = "market_cap";
    };

    private View.OnClickListener rbtnGrowthPercentOnClickListener = v -> {
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
        popup_window.getView(R.id.btn_hour).performClick();
    };

    private View.OnClickListener btnHourOnClickListener = v -> {
        ((RadioButton) popup_window.getView(R.id.rbtn_growth_percent)).setChecked(true);
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
        sorting_parameter = "hour";
    };

    private View.OnClickListener btnDayOnClickListener = v -> {
        ((RadioButton) popup_window.getView(R.id.rbtn_growth_percent)).setChecked(true);
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
        sorting_parameter = "day";
    };

    private View.OnClickListener btnWeekOnClickListener = v -> {
        ((RadioButton) popup_window.getView(R.id.rbtn_growth_percent)).setChecked(true);
        ((SegmentedGroup) popup_window.getView(R.id.segmented_btn_price_change))
                .setTintColor(context.getResources().getColor(R.color.colorPopupSelectedSegmentedBtn));
        sorting_parameter = "week";
    };

    private View.OnClickListener btnUpOnClickListener = v -> {
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_up),
                R.color.colorPopupBtnSelected);
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_down),
                R.color.colorPopupBtnUnselected);
        sorting_direction = "ascending";
    };

    private View.OnClickListener btnDownOnClickListener = v -> {
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_down),
                R.color.colorPopupBtnSelected);
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_up),
                R.color.colorPopupBtnUnselected);
        sorting_direction = "descending";
    };

    private View.OnClickListener btnOkOnClickListener = v -> {

        if (!sorting_direction.isEmpty() && !sorting_parameter.isEmpty()) {
            App.getAppInstance().getPreferences().setSortingParameter(sorting_parameter);
            App.getAppInstance().getPreferences().setSortingDirection(sorting_direction);
        }

        popup_window.dismiss();
        tickerFragment.addAllTickers(
                tickerFragment.getPresenter().getTickers(),
                tickerFragment.getPresenter().getSubscribes());
    };


    private void setBtnBackgroundColor(ImageButton btn, int bg_color) {
        Drawable bg = DrawableCompat.wrap(btn.getBackground());
        DrawableCompat.setTint(bg, context.getResources().getColor(bg_color));
    }

}
