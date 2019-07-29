package com.djavid.bitcoinrate.core

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

abstract class BaseDialog : DialogFragment() {

    abstract val layoutId: Int

    abstract fun setupView(view: View): View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId, container, false)

        return setupView(view)
    }

    fun showError(errorId: Int) {
        //todo getContext() or App.getContext() test this
        Toast.makeText(context, getString(errorId), Toast.LENGTH_SHORT).show()
    }

}
