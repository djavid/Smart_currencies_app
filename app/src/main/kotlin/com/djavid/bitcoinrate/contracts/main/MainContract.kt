package com.djavid.bitcoinrate.contracts.main

interface MainContract {
	
	interface Presenter {
		fun init()
	}
	
	interface View {
		fun init(presenter: Presenter)
		fun showCreateLabelDialog()
	}
	
}