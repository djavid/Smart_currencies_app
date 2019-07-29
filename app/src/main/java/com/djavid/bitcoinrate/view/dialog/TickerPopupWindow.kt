package com.djavid.bitcoinrate.view.dialog

import android.content.Context
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.View
import android.widget.ImageButton
import android.widget.RadioButton
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.view.fragment.TickerFragment
import com.zyyoona7.lib.EasyPopup
import com.zyyoona7.lib.HorizontalGravity
import com.zyyoona7.lib.VerticalGravity
import info.hoang8f.android.segmented.SegmentedGroup


class TickerPopupWindow(private val menuItemView: View?, private val context: Context, private val tickerFragment: TickerFragment) {

    private var sorting_parameter = ""
    private var sorting_direction = ""

    private val popup_window: EasyPopup


    private val rbtnTitleOnClickListener = { v ->
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupBtnUnselected))
        sorting_parameter = "title"
    }

    private val rbtnPriceOnClickListener = { v ->
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupBtnUnselected))
        sorting_parameter = "price"
    }

    private val rbtnMarketCapOnClickListener = { v ->
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupBtnUnselected))
        sorting_parameter = "market_cap"
    }

    private val rbtnGrowthPercentOnClickListener = { v ->
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupSelectedSegmentedBtn))
        popup_window.getView<View>(R.id.btn_hour).performClick()
    }

    private val btnHourOnClickListener = { v ->
        (popup_window.getView<View>(R.id.rbtn_growth_percent) as RadioButton).isChecked = true
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupSelectedSegmentedBtn))
        sorting_parameter = "hour"
    }

    private val btnDayOnClickListener = { v ->
        (popup_window.getView<View>(R.id.rbtn_growth_percent) as RadioButton).isChecked = true
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupSelectedSegmentedBtn))
        sorting_parameter = "day"
    }

    private val btnWeekOnClickListener = { v ->
        (popup_window.getView<View>(R.id.rbtn_growth_percent) as RadioButton).isChecked = true
        (popup_window.getView<View>(R.id.segmented_btn_price_change) as SegmentedGroup)
                .setTintColor(context.resources.getColor(R.color.colorPopupSelectedSegmentedBtn))
        sorting_parameter = "week"
    }

    private val btnUpOnClickListener = { v ->
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_up),
                R.color.colorPopupBtnSelected)
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_down),
                R.color.colorPopupBtnUnselected)
        sorting_direction = "ascending"
    }

    private val btnDownOnClickListener = { v ->
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_down),
                R.color.colorPopupBtnSelected)
        setBtnBackgroundColor(popup_window.getView(R.id.imagebutton_up),
                R.color.colorPopupBtnUnselected)
        sorting_direction = "descending"
    }

    private val btnOkOnClickListener = { v ->

        if (!sorting_direction.isEmpty() && !sorting_parameter.isEmpty()) {
            App.appInstance!!.preferences.sortingParameter = sorting_parameter
            App.appInstance!!.preferences.sortingDirection = sorting_direction
        }

        popup_window.dismiss()
        tickerFragment.addAllTickers(
                tickerFragment.presenter!!.tickers,
                tickerFragment.presenter!!.subscribes)
    }


    init {

        popup_window = EasyPopup(context)
                .setContentView<EasyPopup>(R.layout.popup_layout)
                .setFocusAndOutsideEnable<EasyPopup>(true)
                .setBackgroundDimEnable<EasyPopup>(true)
                .setDimValue<EasyPopup>(0.3f)
                .createPopup()

        initListeners()
        setDefaultPopupState()

    }

    fun show() {
        if (menuItemView != null) {
            popup_window.showAtAnchorView(menuItemView, VerticalGravity.BELOW,
                    HorizontalGravity.ALIGN_RIGHT)
        }
    }

    private fun initListeners() {
        popup_window.getView<View>(R.id.rbtn_title).setOnClickListener(rbtnTitleOnClickListener)
        popup_window.getView<View>(R.id.rbtn_price).setOnClickListener(rbtnPriceOnClickListener)
        popup_window.getView<View>(R.id.rbtn_market_cap).setOnClickListener(rbtnMarketCapOnClickListener)
        popup_window.getView<View>(R.id.rbtn_growth_percent).setOnClickListener(rbtnGrowthPercentOnClickListener)

        popup_window.getView<View>(R.id.btn_hour).setOnClickListener(btnHourOnClickListener)
        popup_window.getView<View>(R.id.btn_day).setOnClickListener(btnDayOnClickListener)
        popup_window.getView<View>(R.id.btn_week).setOnClickListener(btnWeekOnClickListener)

        popup_window.getView<View>(R.id.imagebutton_up).setOnClickListener(btnUpOnClickListener)
        popup_window.getView<View>(R.id.imagebutton_down).setOnClickListener(btnDownOnClickListener)

        popup_window.getView<View>(R.id.btn_ok).setOnClickListener(btnOkOnClickListener)
    }

    private fun setDefaultPopupState() {

        val direction = App.appInstance!!.preferences.sortingDirection
        val parameter = App.appInstance!!.preferences.sortingParameter

        when (parameter) {
            "title" -> popup_window.getView<View>(R.id.rbtn_title).performClick()
            "price" -> popup_window.getView<View>(R.id.rbtn_price).performClick()
            "market_cap" -> popup_window.getView<View>(R.id.rbtn_market_cap).performClick()

            "hour" -> popup_window.getView<View>(R.id.btn_hour).performClick()
            "day" -> popup_window.getView<View>(R.id.btn_day).performClick()
            "week" -> popup_window.getView<View>(R.id.btn_week).performClick()
        }

        when (direction) {
            "ascending" -> popup_window.getView<View>(R.id.imagebutton_up).performClick()

            "descending" -> popup_window.getView<View>(R.id.imagebutton_down).performClick()
        }
    }


    private fun setBtnBackgroundColor(btn: ImageButton, bg_color: Int) {
        val bg = DrawableCompat.wrap(btn.background)
        DrawableCompat.setTint(bg, context.resources.getColor(bg_color))
    }

}
