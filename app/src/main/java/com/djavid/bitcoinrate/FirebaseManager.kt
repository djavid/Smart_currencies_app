package com.djavid.bitcoinrate

import android.content.ContentValues.TAG
import android.util.Log
import com.djavid.bitcoinrate.rest.RestDataRepository
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class FirebaseManager : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendTokenToServer(refreshedToken)

    }

    private fun sendTokenToServer(token: String?) {

        val id: Long
        //if not found preference then is default 0
        id = App.appInstance!!.preferences.tokenId

        val dataRepository = RestDataRepository()
        dataRepository.registerToken(token, id)
                .subscribe { response ->

                    if (response.error.isEmpty()) {
                        if (response.id != 0L) {
                            App.appInstance!!.preferences.tokenId = response.id
                            App.appInstance!!.preferences.token = token
                        }
                    }
                }
    }
}
