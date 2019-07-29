package com.djavid.bitcoinrate.core


interface Presenter<V : View, R : Router, InstanceState> {

    val id: String
    var view: V
    var router: R

    fun onStart()
    fun onStop()
    fun saveInstanceState(instanceState: InstanceState)

}
