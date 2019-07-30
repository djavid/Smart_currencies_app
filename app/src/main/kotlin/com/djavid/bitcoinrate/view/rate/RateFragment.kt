package com.djavid.bitcoinrate.view.rate

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.app.KodeinApp
import com.djavid.bitcoinrate.base.BaseFragment
import com.djavid.bitcoinrate.contracts.rate.RateContract
import com.djavid.bitcoinrate.contracts.rate.RateInstanceState
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.Config.chart_options
import com.djavid.bitcoinrate.util.Config.country_coins
import com.djavid.bitcoinrate.util.Config.cryptoCoins
import ir.mirrajabi.searchdialog.core.SearchResultListener
import java.util.*

class RateFragment : BaseFragment() {

    private val TAG = this.javaClass.simpleName
    lateinit var presenter: RateContract.Presenter
    lateinit var chart: RateChartView
    lateinit var selectedChartOption: ChartOption? = null
    
    lateinit var crypto_selected: Coin
    lateinit var country_selected: Coin
    
    
    override val layoutRes = R.layout.fragment_rate
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as KodeinApp).rateComponent(this)
        presenter.init()
    }
    
    override fun onCreateView(view: View): View {
        Log.i(TAG, "setupView()")
        
        setCurrenciesSpinner()

        getSelectedChartOption()
        setChartLabelSelected(selectedChartLabelView)

        val title = resources.getString(R.string.title_search_dialog_name)
        val hint = resources.getString(R.string.title_search_dialog_hint)

        left_panel!!.setOnClickListener { v ->
            val arrayList = ArrayList(Arrays.asList(*cryptoCoins))

            val dialogCompat = SearchDialog(context, title, hint, null, arrayList,
                    SearchResultListener { baseSearchDialogCompat, coin, i ->
                        tv_left_panel!!.text = coin.symbol
                        Glide.with(this@RateFragment)
                                .asBitmap()
                                .load(coin.imageUrl)
                                .into(iv_left_panel!!)
    
                        App.appInstance.preferences.leftSpinnerValue = coin.symbol
                        crypto_selected = coin
    
                        presenter.showRate(false)
                        baseSearchDialogCompat.dismiss()
                    })
            dialogCompat.show()

        }

        right_panel!!.setOnClickListener { v ->
            val arrayList = ArrayList(Arrays.asList(*country_coins))

            val dialogCompat = SearchDialog(context, title, hint, null, arrayList,
                    SearchResultListener { baseSearchDialogCompat, coin, i ->
                        tv_right_panel!!.text = coin.symbol
                        Glide.with(this@RateFragment)
                                .asBitmap()
                                .load(Codes.getCountryImage(coin.symbol))
                                .into(iv_right_panel!!)
    
                        App.appInstance.preferences.rightSpinnerValue = coin.symbol
                        country_selected = coin
    
                        presenter.showRate(false)
                        baseSearchDialogCompat.dismiss()
                    })
            dialogCompat.show()
        }

        return view
    }

    private fun getChart(chartOption: ChartOption?) {
        presenter.showChart(crypto_selected.symbol, country_selected.symbol, chartOption, true)
    }

    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_rate, menu)
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                presenter.refresh()
                presenter.showRate(true)
                Toast.makeText(context, getString(R.string.updating), Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun onStart() {
        presenter = getPresenter(RateFragmentPresenter::class.java)
        presenter.view = this
        presenter.router = activity as Router
        presenter.onStart()

        super.onStart()

        if ((activity as AppCompatActivity).supportActionBar != null)
            (activity as AppCompatActivity).supportActionBar!!.setTitle(R.string.app_name)
    }

    fun onStop() {
        presenter.saveInstanceState(RateInstanceState(selectedChartOption))
        presenter.view = null
        presenter.onStop()

        super.onStop()
    }

    fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun getTopPanel(): TextView? {
        return topPanel
    }

    //    @Override
    //    public void onRefresh() {
    //        presenter.refresh();
    //    }

    //    @Override
    //    public SwipeRefreshLayout getRefreshLayout() {
    //        return swipe_container;
    //    }
    
    override fun getRateChart(): RateChartView {
        return chart
    }


    override fun getSelectedChartOption(): ChartOption? {
        if (selectedChartOption == null) setSelectedChartOption(chart_options[0])

        return selectedChartOption
    }

    override fun setSelectedChartOption(chartOption: ChartOption) {
        selectedChartOption = chartOption
    }

    override fun setChartLabelSelected(view: View?) {

        when (view!!.id) {
            R.id.optionFirst -> setSelectedChartOption(chart_options[0])
            R.id.optionSecond -> setSelectedChartOption(chart_options[1])
            R.id.optionThird -> setSelectedChartOption(chart_options[2])
            R.id.optionFourth -> setSelectedChartOption(chart_options[3])
        }

        optionFirst!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
        optionSecond!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
        optionThird!!.background = resources.getDrawable(R.drawable.bg_label_unselected)
        optionFourth!!.background = resources.getDrawable(R.drawable.bg_label_unselected)

        view.background = resources.getDrawable(R.drawable.bg_label_selected)
    }

    override fun getSelectedChartLabelView(): TextView? {

        return if (selectedChartOption == chart_options[0])
            optionFirst
        else if (selectedChartOption == chart_options[1])
            optionSecond
        else if (selectedChartOption == chart_options[2])
            optionThird
        else if (selectedChartOption == chart_options[3])
            optionFourth
        else
            null
    }


    override fun getCrypto_selected(): Coin {
        return crypto_selected
    }

    override fun getCountry_selected(): Coin {
        return country_selected
    }

    companion object {

        fun newInstance(): RateFragment {
            return RateFragment()
        }
    }
}












