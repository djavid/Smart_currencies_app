package com.djavid.bitcoinrate.util

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.View

import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class RateChart(view: View) {

    var chart: CombinedChart? = null
    private val color = App.context.resources.getColor(R.color.colorChart)
    private val colorRed = App.context.resources.getColor(R.color.colorPriceChangeNeg)
    private val colorGreen = App.context.resources.getColor(R.color.colorPriceChangePos)
    private val colorPrimary = App.context.resources.getColor(R.color.colorPrimary)
    private val TAG = this.javaClass.simpleName


    init {
        chart = view.findViewById(R.id.chart)
    }


    fun initialize() {
        Log.i(TAG, "initialize()")

        val desc = Description()
        desc.text = ""
        chart!!.setNoDataText(chart!!.resources.getString(R.string.no_data_text))
        chart!!.description = desc
        chart!!.legend.isEnabled = false
        chart!!.axisRight.setDrawLabels(false)

        //chart grid
        chart!!.xAxis.setDrawGridLines(false)
        chart!!.axisLeft.setDrawGridLines(false)
        chart!!.axisRight.setDrawGridLines(false)

        //chart borders
        //chart.getXAxis().setDrawAxisLine(false);
        //chart.getAxisLeft().setDrawAxisLine(false);
        chart!!.axisRight.setDrawAxisLine(false)

        //values typeface
        chart!!.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart!!.xAxis.typeface = Typeface.createFromAsset(chart!!.context.assets, "roboto-regular.ttf")
        chart!!.xAxis.textColor = chart!!.context.resources.getColor(R.color.colorPrimaryText)
        chart!!.xAxis.textSize = 10f

        //chart.getAxisLeft().setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        chart!!.axisLeft.typeface = Typeface.createFromAsset(chart!!.context.assets, "roboto-regular.ttf")
        chart!!.axisLeft.textColor = chart!!.context.resources.getColor(R.color.colorPrimaryText)
        chart!!.axisLeft.textSize = 11f

        //XAxis xAxis = chart.getXAxis();
        //xAxis.setValueFormatter(new DateFormatter(chart));

        chart!!.setPinchZoom(true)
        chart!!.setBackgroundColor(Color.WHITE)
        chart!!.isHighlightPerDragEnabled = false

        chart!!.invalidate()
    }

    fun setData(candleEntries: List<CandleEntry>, lineEntries: List<Entry>, dates: List<Long>) {
        Log.i(TAG, "setData()")

        val candleDataSet = CandleDataSet(candleEntries, "")
        candleDataSet.setDrawIcons(false)
        candleDataSet.axisDependency = YAxis.AxisDependency.LEFT
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = colorRed
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = colorGreen
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.neutralColor = colorPrimary
        candleDataSet.showCandleBar = true
        candleDataSet.setDrawValues(false)
        val candleData = CandleData(candleDataSet)

        val dataSet = LineDataSet(lineEntries, "")
        dataSet.setDrawIcons(false)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.setDrawCircles(false)
        dataSet.setDrawCircleHole(true)
        dataSet.setDrawFilled(true)
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.lineWidth = 2f
        dataSet.setDrawValues(false)
        val lineData = LineData(dataSet)

        val combinedData = CombinedData()
        //combinedData.setData(lineData);
        combinedData.setData(candleData)

        val xAxis = chart!!.xAxis
        xAxis.valueFormatter = DateFormatter(chart, dates)
        chart!!.data = combinedData
        chart!!.invalidate()
    }

}
