package com.djavid.bitcoinrate.core


abstract class BasePresenter<V : View, R : Router, I> : Presenter<V, R, I> {

    override var view: V? = null
    override var router: R? = null
    var instanceState: I? = null
    private val id: String? = null

    constructor(view: V, router: R) {
        this.view = view
        this.router = router
    }

    constructor()

    abstract override fun onStart()

    abstract override fun onStop()

    abstract override fun saveInstanceState(instanceState: I)
}
