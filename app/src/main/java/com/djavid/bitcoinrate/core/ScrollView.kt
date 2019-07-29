package com.djavid.bitcoinrate.core


interface ScrollView<T> : View {
    fun scrollToPosition(position: Int)
    fun addView(item: T)
    fun resetFeed()
}
