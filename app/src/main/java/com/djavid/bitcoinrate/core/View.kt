package com.djavid.bitcoinrate.core

interface View {
    fun showProgressbar()
    fun hideProgressbar()
    fun showError(errorId: Int)
    fun dispose()
}
