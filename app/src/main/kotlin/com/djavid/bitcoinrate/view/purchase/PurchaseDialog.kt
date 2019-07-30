package com.djavid.bitcoinrate.view.purchase

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.view.main.MainActivity


class PurchaseDialog : BaseDialog() {

    @BindView(R.id.tv_title)
    internal var tv_title: TextView? = null
    @BindView(R.id.tv_purchase_info_1)
    internal var tv_purchase_info_1: TextView? = null
    @BindView(R.id.tv_purchase_info_2)
    internal var tv_purchase_info_2: TextView? = null
    @BindView(R.id.btn_purchase)
    internal var btn_purchase: Button? = null

    override val layoutId: Int
        get() = R.layout.fragment_purchase_dialog

    override fun setupView(view: View): View {

        tv_purchase_info_1!!.text = getText(R.string.purchase_info_1)
        tv_purchase_info_2!!.text = getText(R.string.purchase_info_2)

        btn_purchase!!.setOnClickListener { v ->

            val activity = activity
            if (activity is MainActivity) {
                activity.purchase()
            }

        }

        return view
    }

    companion object {

        fun newInstance(): PurchaseDialog {
            val fragment = PurchaseDialog()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}
