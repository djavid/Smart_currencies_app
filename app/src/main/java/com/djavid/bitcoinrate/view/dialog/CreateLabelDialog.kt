package com.djavid.bitcoinrate.view.dialog

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.core.BaseDialog
import com.djavid.bitcoinrate.model.heroku.Subscribe
import com.djavid.bitcoinrate.model.project.LabelItemDto
import com.djavid.bitcoinrate.rest.RestDataRepository
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.PriceConverter
import com.djavid.bitcoinrate.util.RxUtils
import com.djavid.bitcoinrate.view.activity.MainActivity
import com.djavid.bitcoinrate.view.adapter.TickerItem
import com.tomergoldst.tooltips.ToolTip
import com.tomergoldst.tooltips.ToolTipsManager


class CreateLabelDialog : BaseDialog() {

    @BindView(R.id.et_price)
    internal var et_price: EditText? = null
    @BindView(R.id.tv_cancel_btn)
    internal var tv_cancel_btn: TextView? = null
    @BindView(R.id.tv_create_btn)
    internal var tv_create_btn: TextView? = null
    @BindView(R.id.cb_percent_change)
    internal var cb_percent_change: CheckBox? = null
    @BindView(R.id.tv_price_value)
    internal var tv_price: TextView? = null

    @BindView(R.id.tv_change)
    internal var tv_change: TextView? = null
    @BindView(R.id.tv_change_value)
    internal var tv_change_value: TextView? = null
    @BindView(R.id.iv_help)
    internal var iv_help: ImageView? = null
    @BindView(R.id.rl_label_dialog)
    internal var rl_label_dialog: RelativeLayout? = null

    private var mToolTipsManager: ToolTipsManager? = null
    private var tooltipView: View? = null

    internal var addBtnOnCLickListener: View.OnClickListener = View.OnClickListener {
        try {
            val selectedTicker = (activity as MainActivity).selectedTickerItem

            if (selectedTicker == null || selectedTicker.tickerItem == null) {
                showError(R.string.error_occurred)
                dismiss()
                return@OnClickListener
            }

            val value = et_price!!.text.toString()
            val isPercentLabel = cb_percent_change!!.isChecked

            if (!isValidValue(value, isPercentLabel)) {
                return@OnClickListener
            }

            val value_double = java.lang.Double.parseDouble(value)
            val isTrendingUp = value_double > selectedTicker.tickerItem!!.ticker!!.price

            val token_id = App.appInstance!!.preferences.tokenId
            val ticker_id = selectedTicker.tickerItem!!.id
            val cryptoId = selectedTicker.tickerItem!!.cryptoId
            val countryId = selectedTicker.tickerItem!!.countryId

            val labelItemDto: LabelItemDto
            val subscribe: Subscribe

            if (isPercentLabel) {

                for (item in selectedTicker.labels!!) {
                    if (item.change_percent == value_double / 100) {
                        showError(R.string.error_subscribe_already_added)
                        return@OnClickListener
                    }
                }

                val perc = java.lang.Double.toString(java.lang.Double.parseDouble(value) / 100)
                subscribe = Subscribe(perc, ticker_id, token_id, cryptoId, countryId,
                        selectedTicker.tickerItem!!.ticker!!.price)
                labelItemDto = LabelItemDto(perc, isTrendingUp, true)

            } else {

                for (item in selectedTicker.labels!!) {
                    if (item.change_percent == 0.0) {
                        println(item.value + " " + value_double)
                        if (java.lang.Double.parseDouble(item.value) == value_double) {
                            showError(R.string.error_subscribe_already_added)
                            return@OnClickListener
                        }
                    }
                }

                subscribe = Subscribe(isTrendingUp, value, ticker_id, token_id, cryptoId, countryId)
                labelItemDto = LabelItemDto(value, isTrendingUp, false)
            }

            sendSubscribe(subscribe, labelItemDto, selectedTicker)
            dismiss()

        } catch (e: Exception) {
            e.printStackTrace()
            showError(R.string.error_occurred)
        }
    }

    internal var percentOnCheckedChangeListener: CompoundButton.OnCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if (isChecked) {

            tv_change!!.visibility = View.VISIBLE
            tv_change_value!!.visibility = View.VISIBLE
            tv_change_value!!.text = "±0"
            et_price!!.setHint(R.string.title_hint_percent)

        } else {

            tv_change!!.visibility = View.GONE
            tv_change_value!!.visibility = View.GONE
            et_price!!.setHint(R.string.title_hint_price)
        }
    }

    internal var helpBtnOnClickListener = { v ->

        if (tooltipView != null && mToolTipsManager!!.isVisible(tooltipView!!)) {

            mToolTipsManager!!.dismiss(tooltipView, true)

        } else {

            if (context != null) {

                val hint: String
                if (cb_percent_change!!.isChecked)
                    hint = getString(R.string.hint_percent)
                else
                    hint = getString(R.string.hint_fixed_price)

                val builder = ToolTip.Builder(context!!, iv_help,
                        rl_label_dialog, hint, ToolTip.POSITION_BELOW)

                builder.setBackgroundColor(resources.getColor(R.color.colorSettingsAccent))

                tooltipView = mToolTipsManager!!.show(builder.build())
            }
        }

    }

    internal var textWatcherPercent: TextWatcher = object : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable) {

            mToolTipsManager!!.dismissAll()

            if (cb_percent_change!!.isChecked) {

                if (s.toString().isEmpty()) {
                    tv_change_value!!.text = "±0 "
                    return
                }

                try {
                    val selectedTicker = (activity as MainActivity).selectedTickerItem

                    val price = selectedTicker!!.tickerItem!!.ticker!!.price
                    val change = price * (java.lang.Double.parseDouble(s.toString()) / 100.0)

                    val text = "±" + PriceConverter.convertPrice(change) + " " +
                            Codes.getCountrySymbol(selectedTicker.tickerItem!!.countryId!!)
                    tv_change_value!!.text = text

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_create_label_dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mToolTipsManager = ToolTipsManager()
    }

    override fun setupView(view: View): View {

        try {
            val selectedTicker = (activity as MainActivity).selectedTickerItem

            val price = (PriceConverter.convertPrice(
                    selectedTicker!!.tickerItem!!.ticker!!.price) + " "
                    + selectedTicker.tickerItem!!.countryId)
            tv_price!!.text = price

        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        tv_change!!.visibility = View.GONE
        tv_change_value!!.visibility = View.GONE

        tv_cancel_btn!!.setOnClickListener { v -> this.dismiss() }

        tv_create_btn!!.setOnClickListener(addBtnOnCLickListener)

        cb_percent_change!!.setOnCheckedChangeListener(percentOnCheckedChangeListener)

        iv_help!!.setOnClickListener(helpBtnOnClickListener)

        et_price!!.addTextChangedListener(textWatcherPercent)

        return view
    }

    private fun isValidValue(value: String, isPercentLabel: Boolean): Boolean {

        if (value.isEmpty()) {
            showErrorHint(R.string.error_empty_value)
            return false
        }

        if (value.startsWith(".") || value.endsWith(".")) {
            showErrorHint(R.string.error_wrong_format)
            return false
        }

        if (value.contains("-") || value.contains(" ")) {
            showErrorHint(R.string.error_wrong_format)
            return false
        }

        if (isPercentLabel) {
            try {
                val perc = java.lang.Double.parseDouble(value)

                if (perc > 200 || perc <= 0) {
                    showErrorHint(R.string.error_percent_max)
                    return false
                }

                if (value.contains(".")) {
                    if (value.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].length > 4) {
                        showErrorHint(R.string.error_percent_double)
                        return false
                    }
                }

            } catch (e: NumberFormatException) {
                e.printStackTrace()
                return false
            }

        }

        return true
    }

    private fun sendSubscribe(subscribe: Subscribe, label: LabelItemDto, tickerItem: TickerItem?) {

        val dataRepository = RestDataRepository()

        println(subscribe)
        dataRepository.sendSubscribe(subscribe)
                .compose<ResponseId>(RxUtils.applySingleSchedulers<ResponseId>())
                .subscribe({ response ->

                    if (response.error.isEmpty()) {
                        Log.d("LabelDialog", "Successfully sent $subscribe")

                        var amount = App.appInstance!!.preferences.subscribesAmount
                        App.appInstance!!.preferences.subscribesAmount = ++amount

                        if (response.id != 0L) {
                            label.id = response.id
                            tickerItem!!.addLabelItem(label)
                        }
                    } else {
                        Log.e("LabelDialog", response.error)
                        showError(R.string.connection_error)
                    }

                }, { error -> showError(R.string.connection_error) })
    }

    private fun showErrorHint(res: Int) {

        val hint = getString(res)

        val builder = ToolTip.Builder(context!!, et_price,
                rl_label_dialog, hint, ToolTip.POSITION_BELOW)
        builder.setBackgroundColor(resources.getColor(R.color.colorPriceChangeNeg))

        mToolTipsManager!!.dismissAll()
        tooltipView = mToolTipsManager!!.show(builder.build())
    }

    companion object {

        fun newInstance(): CreateLabelDialog {

            val fragment = CreateLabelDialog()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }

}