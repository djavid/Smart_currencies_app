package com.djavid.bitcoinrate.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.app.App
import com.zyyoona7.lib.EasyPopup
import com.zyyoona7.lib.HorizontalGravity
import com.zyyoona7.lib.VerticalGravity

class SettingsFragment : Fragment() {
    
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
    
        btn_hour.setOnClickListener(percentOnClickListener)
        btn_day.setOnClickListener(percentOnClickListener)
        btn_week.setOnClickListener(percentOnClickListener)
    
        btn_titles.setOnClickListener(titleFormatOnClickListener)
        btn_codes.setOnClickListener(titleFormatOnClickListener)
    
        btn_about_developer.setOnClickListener(aboutBtnOnClickListener)
    
        switch_sound.setOnCheckedChangeListener { buttonView, isChecked -> App.appInstance.preferences.notificationSound = isChecked }
    
        switch_vibration.setOnCheckedChangeListener { buttonView, isChecked -> App.appInstance.preferences.notificationVibration = isChecked }

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
