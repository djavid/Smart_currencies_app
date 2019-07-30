package com.djavid.bitcoinrate.view.ticker

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.djavid.bitcoinrate.model.project.LabelItemDto
import com.djavid.bitcoinrate.network.RestDataRepository
import com.djavid.bitcoinrate.view.main.MainActivity
import com.mindorks.placeholderview.PlaceHolderView
import com.mindorks.placeholderview.annotations.Click
import com.mindorks.placeholderview.annotations.Layout
import com.mindorks.placeholderview.annotations.NonReusable
import com.mindorks.placeholderview.annotations.Resolve
import com.mindorks.placeholderview.annotations.View


@NonReusable
@Layout(R.layout.ticker_label_item)
internal class LabelItem(private val mContext: Context, private val mPlaceHolderView: PlaceHolderView, var labelItemDto: LabelItemDto,
                         private val tickerItem: TickerItem) {

    @View(R.id.tv_ticker_label)
    private val tv_ticker_label: TextView? = null
    @View(R.id.iv_label_trending)
    private val iv_label_trending: ImageView? = null
    @View(R.id.iv_label_add)
    private val iv_label_add: ImageView? = null
    @View(R.id.ll_label_btn)
    private val ll_label_btn: LinearLayout? = null

    private val isAddButton: Boolean?


    init {

        isAddButton = labelItemDto.isAddButton
    }


    @Resolve
    private fun onResolved() {

        if (!labelItemDto.isAddButton) {

            if (labelItemDto.isPercentLabel) {

                val text = (labelItemDto.change_percent * 100).toString() + " %"
                tv_ticker_label!!.text = text

                tv_ticker_label.visibility = android.view.View.VISIBLE
                iv_label_trending!!.visibility = android.view.View.GONE
                iv_label_add!!.visibility = android.view.View.GONE

            } else {

                tv_ticker_label!!.text = labelItemDto.value

                if (labelItemDto.isTrendingUp)
                    iv_label_trending!!.setImageResource(R.drawable.ic_trending_up_white_24px)
                else
                    iv_label_trending!!.setImageResource(R.drawable.ic_trending_down_white_24px)

                tv_ticker_label.visibility = android.view.View.VISIBLE
                iv_label_trending.visibility = android.view.View.VISIBLE
                iv_label_add!!.visibility = android.view.View.GONE
            }

        } else {

            tv_ticker_label!!.visibility = android.view.View.GONE
            tv_ticker_label.text = ""
            iv_label_trending!!.visibility = android.view.View.GONE
            iv_label_add!!.visibility = android.view.View.VISIBLE

        }

    }

    @Click(R.id.ll_label_btn)
    private fun onClick() {

        if (isAddButton!!) {
            (mContext as MainActivity).showCreateLabelDialog(tickerItem)
        } else {
            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("Удалить оповещение")
            alert.setMessage("Вы действительно хотите удалить это оповещение?")

            alert.setPositiveButton("Да") { dialog, which -> deleteSubscribe(labelItemDto.id) }

            alert.setNegativeButton("Нет") { dialog, which -> dialog.dismiss() }

            alert.show()
        }
    }

    private fun deleteSubscribe(id: Long?) {

        val dataRepository = RestDataRepository()

        dataRepository.deleteSubscribe(id!!)
                .subscribe({
                    tickerItem.deleteLabel(this)

                    var amount = App.appInstance!!.preferences.subscribesAmount
                    App.appInstance!!.preferences.subscribesAmount = --amount

                    Log.d("LabelDialog", "Successfully deleted subscribe with id = $id")
                }, { error -> showError(R.string.error_deleting_subscribe) })
    }

    private fun showError(errorId: Int) {
        Toast.makeText(App.context,
                App.context.getString(errorId), Toast.LENGTH_SHORT).show()
    }

}
