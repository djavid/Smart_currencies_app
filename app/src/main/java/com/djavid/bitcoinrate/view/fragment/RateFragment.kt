package com.djavid.bitcoinrate.view.fragment

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import com.bumptech.glide.Glide
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.core.BaseDialog
import com.djavid.bitcoinrate.core.Router
import com.djavid.bitcoinrate.model.project.ChartOption
import com.djavid.bitcoinrate.model.project.Coin
import com.djavid.bitcoinrate.presenter.instancestate.RateFragmentInstanceState
import com.djavid.bitcoinrate.presenter.interfaces.RateFragmentPresenter
import com.djavid.bitcoinrate.util.Codes
import com.djavid.bitcoinrate.util.Config.chart_options
import com.djavid.bitcoinrate.util.Config.country_coins
import com.djavid.bitcoinrate.util.Config.crypto_coins
import com.djavid.bitcoinrate.util.RateChart
import com.djavid.bitcoinrate.view.dialog.SearchDialog
import com.djavid.bitcoinrate.view.interfaces.RateFragmentView
import ir.mirrajabi.searchdialog.core.SearchResultListener
import java.util.*


class RateFragment : BaseDialog(), RateFragmentView
//        , SwipeRefreshLayout.OnRefreshListener
{

    //    @BindView(R.id.swipe_container)
    //    SwipeRefreshLayout swipe_container;

    @BindView(R.id.topPanel)
    internal var topPanel: TextView? = null

    @BindView(R.id.left_panel)
    internal var left_panel: LinearLayout? = null
    @BindView(R.id.right_panel)
    internal var right_panel: LinearLayout? = null
    @BindView(R.id.iv_left_panel)
    internal var iv_left_panel: ImageView? = null
    @BindView(R.id.tv_left_panel)
    internal var tv_left_panel: TextView? = null
    @BindView(R.id.iv_right_panel)
    internal var iv_right_panel: ImageView? = null
    @BindView(R.id.tv_right_panel)
    internal var tv_right_panel: TextView? = null

    @BindView(R.id.optionFirst)
    internal var optionFirst: TextView? = null
    @BindView(R.id.optionSecond)
    internal var optionSecond: TextView? = null
    @BindView(R.id.optionThird)
    internal var optionThird: TextView? = null
    @BindView(R.id.optionFourth)
    internal var optionFourth: TextView? = null

    private val TAG = this.javaClass.simpleName
    internal var presenter: RateFragmentPresenter
    internal var chart: RateChart
    internal var selectedChartOption: ChartOption? = null

    internal var crypto_selected: Coin
    internal var country_selected: Coin

    internal var onChartOptionClick = { v ->
        setChartLabelSelected(v)
        getChart(getSelectedChartOption())
    }

    val layoutId: Int
        get() = R.layout.fragment_rate

    val presenterId: String
        get() = "rate_fragment"

    fun loadData() {

    }

    fun setupView(view: View): View {
        Log.i(TAG, "setupView()")

        //        swipe_container.setOnRefreshListener(this);
        //        swipe_container.setColorSchemeColors(
        //                getResources().getColor(R.color.colorAccent),
        //                getResources().getColor(R.color.colorChart),
        //                getResources().getColor(R.color.colorLabelBackground));

        view.findViewById<View>(R.id.optionFirst).setOnClickListener(onChartOptionClick)
        view.findViewById<View>(R.id.optionSecond).setOnClickListener(onChartOptionClick)
        view.findViewById<View>(R.id.optionThird).setOnClickListener(onChartOptionClick)
        view.findViewById<View>(R.id.optionFourth).setOnClickListener(onChartOptionClick)

        chart = RateChart(view)
        chart.initialize()

        setCurrenciesSpinner()

        getSelectedChartOption()
        setChartLabelSelected(selectedChartLabelView)

        val title = resources.getString(R.string.title_search_dialog_name)
        val hint = resources.getString(R.string.title_search_dialog_hint)

        left_panel!!.setOnClickListener { v ->
            val arrayList = ArrayList(Arrays.asList(*crypto_coins))

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

    override fun setCurrenciesSpinner() {

        val left_value = App.appInstance.preferences.leftSpinnerValue
        val right_value = App.appInstance.preferences.rightSpinnerValue

        crypto_selected = Codes.getCryptoCoinByCode(left_value)
        country_selected = Codes.getCountryCoinByCode(right_value)

        tv_left_panel!!.text = left_value
        tv_right_panel!!.text = right_value

        Glide.with(this)
                .asBitmap()
                .load(crypto_selected.imageUrl)
                .into(iv_left_panel!!)

        Glide.with(this)
                .asBitmap()
                .load(Codes.getCountryImage(right_value))
                .into(iv_right_panel!!)

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
        presenter.saveInstanceState(RateFragmentInstanceState(selectedChartOption))
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

    override fun getRateChart(): RateChart {
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












