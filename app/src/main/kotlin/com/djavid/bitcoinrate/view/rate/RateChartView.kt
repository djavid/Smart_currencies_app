package com.djavid.bitcoinrate.view.rate

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.contracts.rate.RateContract
import com.djavid.bitcoinrate.util.DateFormatter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_rate.view.*

class RateChartView(private val viewRoot: View) : RateContract.ChartView {
    
    private val colorRed = ContextCompat.getColor(viewRoot.context, R.color.colorPriceChangeNeg)
    private val colorGreen = ContextCompat.getColor(viewRoot.context, R.color.colorPriceChangePos)
    private val colorPrimary = ContextCompat.getColor(viewRoot.context, R.color.colorPrimary)
    private val colorPrimaryText = ContextCompat.getColor(viewRoot.context, R.color.colorPrimaryText)
    
    private val robotoTypeface = Typeface.createFromAsset(viewRoot.context.assets, "roboto-regular.ttf")
    
    private val tag = this.javaClass.simpleName
    
    override fun init() {
        Log.i(tag, "init()")
        
        viewRoot.chart.apply {
            setNoDataText(resources.getString(R.string.no_data_text))
            description = Description().apply { text = "" }
            legend.isEnabled = false
            axisRight.setDrawLabels(false)
            
            //chart grid
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            
            //values typeface
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.typeface = robotoTypeface
            xAxis.textColor = colorPrimaryText
            xAxis.textSize = 10f
            
            axisLeft.typeface = robotoTypeface
            axisLeft.textColor = colorPrimaryText
            axisLeft.textSize = 11f
            
            setPinchZoom(true)
            setBackgroundColor(Color.WHITE)
            isHighlightPerDragEnabled = false
            
            invalidate()
        }
    }
    
    override fun setData(candleEntries: List<CandleEntry>, lineEntries: List<Entry>, dates: List<Long>) {
        Log.i(tag, "setData()")
        
        viewRoot.chart.apply {
            val candleDataSet = CandleDataSet(candleEntries, "").apply {
                setDrawIcons(false)
                axisDependency = YAxis.AxisDependency.LEFT
                shadowColor = Color.DKGRAY
                shadowWidth = 0.7f
                decreasingColor = colorRed
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = colorGreen
                increasingPaintStyle = Paint.Style.FILL
                neutralColor = colorPrimary
                showCandleBar = true
                setDrawValues(false)
            }
            
            val dataSet = LineDataSet(lineEntries, "").apply {
                setDrawIcons(false)
                axisDependency = YAxis.AxisDependency.LEFT
                setDrawCircles(false)
                setDrawCircleHole(true)
                setDrawFilled(true)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                lineWidth = 2f
                setDrawValues(false)
            }
            
            val combinedData = CombinedData().apply {
                val candleData = CandleData(candleDataSet)
                setData(candleData)
            }
            
            xAxis.valueFormatter = DateFormatter(this, dates)
            data = combinedData
            invalidate()
        }
    }

}
