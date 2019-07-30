package com.djavid.bitcoinrate.view.main

import android.graphics.Color
import android.view.View
import androidx.fragment.app.FragmentManager
import com.djavid.bitcoinrate.R
import com.djavid.bitcoinrate.TAG_CREATE_LABEL_DIALOG
import com.djavid.bitcoinrate.contracts.main.MainContract
import com.djavid.bitcoinrate.view.ticker.CreateLabelDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.toolbar.view.*

class MainView(
		private val viewRoot: View,
		private val adapter: MainPagerAdapter,
		private val fragmentManager: FragmentManager
) : MainContract.View {
	
	private lateinit var presenter: MainContract.Presenter
	
	override fun init(presenter: MainContract.Presenter) {
		this.presenter = presenter
		
		viewRoot.toolbar.setTitleTextColor(Color.WHITE)
		
		setViewpager()
		setNavigationView()
	}
	
	private fun setViewpager() {
		viewRoot.viewpager.adapter = adapter
	}
	
	private fun setNavigationView() {
		viewRoot.navigation.apply {
			onNavigationItemSelectedListener = mOnNavigationItemSelectedListener
			selectedItemId = R.id.navigation_rate
			
			navigation.enableAnimation(false)
			navigation.enableShiftingMode(false)
			navigation.enableItemShiftingMode(false)
			navigation.setTextVisibility(false)
			
			val iconSize = 30
			navigation.setIconSize(iconSize.toFloat(), iconSize.toFloat())
			navigation.itemHeight = BottomNavigationViewEx.dp2px(viewRoot.context, (iconSize + 16).toFloat())
		}
	}
	
	private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
		when (item.itemId) {
			R.id.navigation_rate -> {
				viewRoot.viewpager.currentItem = 0
				return@OnNavigationItemSelectedListener true
			}
			R.id.navigation_tickers -> {
				viewRoot.viewpager.currentItem = 1
				return@OnNavigationItemSelectedListener true
			}
			R.id.navigation_settings -> {
				viewRoot.viewpager.currentItem = 2
				return@OnNavigationItemSelectedListener true
			}
		}
		
		false
	}
	
	override fun showCreateLabelDialog() {
		val dialog = CreateLabelDialog.newInstance()
		dialog.show(fragmentManager, TAG_CREATE_LABEL_DIALOG)
	}
	
	//	fun showError(errorId: Int) {
//		try {
//			runOnUiThread { Toast.makeText(this, getString(errorId), Toast.LENGTH_SHORT).show() }
//		} catch (e: Exception) {
//			e.printStackTrace()
//		}
//	}
	
}