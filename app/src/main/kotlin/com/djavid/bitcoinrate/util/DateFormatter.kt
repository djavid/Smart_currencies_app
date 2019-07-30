package com.djavid.bitcoinrate.util

import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class DateFormatter internal constructor(private val chart: BarLineChartBase<*>, private val dates: List<Long>) : IAxisValueFormatter {

    override fun getFormattedValue(value: Float, axis: AxisBase): String {

        try {

            if (value >= dates.size || value < 0) return ""

            val date = Date(dates[value.toInt()] * 1000)
            val hour = SimpleDateFormat("HH", Locale.US).format(date)
            val minute = SimpleDateFormat("mm", Locale.US).format(date)
            val day = Integer.parseInt(SimpleDateFormat("dd", Locale.US).format(date))
            val month = Integer.parseInt(SimpleDateFormat("MM", Locale.US).format(date))
            val year = Integer.parseInt(SimpleDateFormat("yy", Locale.US).format(date))

            val monthName = App.context.resources.getStringArray(R.array.month_titles)[month - 1] //TODO check
            val yearName = year.toString()

            val high_x = chart.highestVisibleX.toInt()
            val low_x = chart.lowestVisibleX.toInt()
            if (high_x >= dates.size || high_x < 0 || low_x >= dates.size || low_x < 0)
                return ""
            val visible_range = Math.abs(dates[high_x] - dates[low_x]).toFloat()
            val days_count = visible_range / 3600f / 24f

            //            System.out.println(chart.getVisibleXRange() + "-------------------------");
            //            System.out.println(visible_range);
            //            System.out.println(days_count);

            return if (days_count >= 180) {

                monthName + " " + yearName

            } else if (days_count >= 2 && days_count < 180) {

                if (day == 0) "" else "$day $monthName"

            } else if (days_count < 2 && days_count > 0) {

                "$hour:$minute"

            } else {
                ""
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }


}