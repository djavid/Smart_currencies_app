package com.djavid.bitcoinrate.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Switch
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.djavid.bitcoinrate.App
import com.djavid.bitcoinrate.R
import com.zyyoona7.lib.EasyPopup
import com.zyyoona7.lib.HorizontalGravity
import com.zyyoona7.lib.VerticalGravity


class SettingsFragment : Fragment() {

    @BindView(R.id.btn_hour)
    internal var btn_hour: RadioButton? = null
    @BindView(R.id.btn_day)
    internal var btn_day: RadioButton? = null
    @BindView(R.id.btn_week)
    internal var btn_week: RadioButton? = null

    @BindView(R.id.btn_codes)
    internal var btn_codes: RadioButton? = null
    @BindView(R.id.btn_titles)
    internal var btn_titles: RadioButton? = null

    @BindView(R.id.btn_about_developer)
    internal var btn_about_developer: Button? = null
    @BindView(R.id.ll_parent)
    internal var ll_parent: LinearLayout? = null

    @BindView(R.id.switch_sound)
    internal var switch_sound: Switch? = null
    @BindView(R.id.switch_vibration)
    internal var switch_vibration: Switch? = null


    private var unbinder: Unbinder? = null
    private val TAG = this.javaClass.simpleName

    internal var aboutBtnOnClickListener = { v ->

        val popup_window = EasyPopup(context)
                .setContentView<EasyPopup>(R.layout.about_developer)
                .setFocusAndOutsideEnable<EasyPopup>(true)
                .setBackgroundDimEnable<EasyPopup>(true)
                .setDimValue<EasyPopup>(0.3f)
                .createPopup<EasyPopup>()

        popup_window.showAtAnchorView(ll_parent!!, VerticalGravity.CENTER, HorizontalGravity.CENTER)

    }

    internal var percentOnClickListener = { v ->

        when (v.getId()) {

            R.id.btn_hour -> App.appInstance.preferences.showedPriceChange = "hour"

            R.id.btn_day -> App.appInstance.preferences.showedPriceChange = "day"

            R.id.btn_week -> App.appInstance.preferences.showedPriceChange = "week"
        }
    }

    internal var titleFormatOnClickListener = { v ->

        when (v.getId()) {

            R.id.btn_codes -> App.appInstance.preferences.titleFormat = "codes"

            R.id.btn_titles -> App.appInstance.preferences.titleFormat = "titles"
        }
    }


    override fun onStart() {
        super.onStart()

        try {
            (activity as AppCompatActivity).supportActionBar!!.setTitle(R.string.title_settings)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder!!.unbind()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        unbinder = ButterKnife.bind(this, view)

        btn_hour!!.setOnClickListener(percentOnClickListener)
        btn_day!!.setOnClickListener(percentOnClickListener)
        btn_week!!.setOnClickListener(percentOnClickListener)

        btn_titles!!.setOnClickListener(titleFormatOnClickListener)
        btn_codes!!.setOnClickListener(titleFormatOnClickListener)

        btn_about_developer!!.setOnClickListener(aboutBtnOnClickListener)

        switch_sound!!.setOnCheckedChangeListener { buttonView, isChecked -> App.appInstance.preferences.notificationSound = isChecked }

        switch_vibration!!.setOnCheckedChangeListener { buttonView, isChecked -> App.appInstance.preferences.notificationVibration = isChecked }

        setButtonsDefaults()

        return view
    }

    private fun setButtonsDefaults() {

        when (App.appInstance.preferences.showedPriceChange) {
            "hour" -> btn_hour!!.isChecked = true
            "day" -> btn_day!!.isChecked = true
            "week" -> btn_week!!.isChecked = true
        }

        when (App.appInstance.preferences.titleFormat) {
            "codes" -> btn_codes!!.isChecked = true
            "titles" -> btn_titles!!.isChecked = true
        }

        switch_sound!!.isChecked = App.appInstance.preferences.notificationSound!!
        switch_vibration!!.isChecked = App.appInstance.preferences.notificationVibration!!
    }

    fun showError(errorId: Int) {
        Toast.makeText(context, getString(errorId), Toast.LENGTH_SHORT).show()
    }

    companion object {

        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }

}
