package com.djavid.bitcoinrate.view.ticker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.heroku.Ticker
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.network.RestDataRepository
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.Config.country_coins
import com.djavid.bitcoinrate.util.Config.cryptoCoins
import com.djavid.bitcoinrate.view.rate.SearchDialog
import ir.mirrajabi.searchdialog.core.SearchResultListener
import java.util.*


class CreateTickerDialog : BaseDialog() {

    @BindView(R.id.tv_cancel_btn)
    internal var tv_cancel_btn: TextView? = null
    @BindView(R.id.tv_create_btn)
    internal var tv_create_btn: TextView? = null

    @BindView(R.id.left_panel_t)
    internal var left_panel: LinearLayout? = null
    @BindView(R.id.right_panel_t)
    internal var right_panel: LinearLayout? = null
    @BindView(R.id.iv_left_panel_t)
    internal var iv_left_panel: ImageView? = null
    @BindView(R.id.tv_left_panel_t)
    internal var tv_left_panel: TextView? = null
    @BindView(R.id.iv_right_panel_t)
    internal var iv_right_panel: ImageView? = null
    @BindView(R.id.tv_right_panel_t)
    internal var tv_right_panel: TextView? = null

    internal var crypto_selected: Coin
    internal var country_selected: Coin

    override val layoutId: Int
        get() = R.layout.fragment_create_ticker_dialog

    override fun setupView(view: View): View {

        tv_cancel_btn!!.setOnClickListener { v -> this.dismiss() }

        tv_create_btn!!.setOnClickListener { v ->

            try {
                val pairs = arguments!!.getStringArrayList("pairs")

                val crypto_symbol = crypto_selected.symbol
                val country_symbol = country_selected.symbol
                val pair = crypto_symbol + country_symbol

                if (pairs != null) {
                    if (pairs.contains(pair)) {
                        showError(R.string.error_pair_already_added)
                    } else {
                        val token_id = App.appInstance!!.preferences.tokenId
                        val ticker = Ticker(token_id, crypto_symbol, country_symbol)

                        sendTicker(ticker)
                        dismiss()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismiss()
            }


        }

        val title = resources.getString(R.string.title_search_dialog_name)
        val hint = resources.getString(R.string.title_search_dialog_hint)

        left_panel!!.setOnClickListener { v ->
            val arrayList = ArrayList(Arrays.asList(*cryptoCoins))

            val dialogCompat = SearchDialog(context, title, hint, null, arrayList,
                    SearchResultListener { baseSearchDialogCompat, coin, i ->
                        crypto_selected = coin
                        tv_left_panel!!.text = coin.symbol
                        Glide.with(this@CreateTickerDialog)
                                .asBitmap()
                                .load(coin.imageUrl)
                                .into(iv_left_panel!!)
                        baseSearchDialogCompat.dismiss()
                    })
            dialogCompat.show()

        }

        right_panel!!.setOnClickListener { v ->
            val arrayList = ArrayList(Arrays.asList(*country_coins))

            val dialogCompat = SearchDialog(context, title, hint, null, arrayList,
                    SearchResultListener { baseSearchDialogCompat, coin, i ->
                        country_selected = coin
                        tv_right_panel!!.text = coin.symbol
                        Glide.with(this@CreateTickerDialog)
                                .asBitmap()
                                .load(Codes.getCountryImage(coin.symbol))
                                .into(iv_right_panel!!)
                        baseSearchDialogCompat.dismiss()
                    })
            dialogCompat.show()
        }

        setCurrenciesSpinner()

        return view
    }


    private fun sendTicker(ticker: Ticker) {

        val dataRepository = RestDataRepository()

        dataRepository.sendTicker(ticker)
                .subscribe({ response ->

                    if (response.error.isEmpty()) {
                        Log.d("TickerDialog", "Succesfully sent $ticker")

                        if (response.id != 0L) {

                            val bundle = Bundle()
                            bundle.putString("cryptoId", ticker.cryptoId)
                            bundle.putString("countryId", ticker.countryId)
                            bundle.putLong("id", response.id)

                            val intent = Intent().putExtras(bundle)
                            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
                        }

                    } else {
                        Log.e("TickerDialog", response.error)
                        showError(R.string.connection_error)
                    }

                }, { error -> showError(R.string.connection_error) })
    }

    fun setCurrenciesSpinner() {
    
        crypto_selected = cryptoCoins[0]
        country_selected = country_coins[0]

        tv_left_panel!!.text = crypto_selected.symbol
        tv_right_panel!!.text = country_selected.symbol

        Glide.with(this)
                .asBitmap()
                .load(crypto_selected.imageUrl)
                .into(iv_left_panel!!)

        Glide.with(this)
                .asBitmap()
                .load(Codes.getCountryImage(country_selected.symbol))
                .into(iv_right_panel!!)
    }

    companion object {

        fun newInstance(pairs: ArrayList<String>): CreateTickerDialog {

            val fragment = CreateTickerDialog()
            val args = Bundle()
            args.putStringArrayList("pairs", pairs)
            fragment.arguments = args
            return fragment
        }
    }

}
