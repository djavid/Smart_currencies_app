package com.djavid.bitcoinrate;

import android.util.Log;

import com.djavid.bitcoinrate.rest.RestDataRepository;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;


public class FirebaseManager extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendTokenToServer(refreshedToken);

    }

    private void sendTokenToServer(String token) {

        long id;
        //if not found preference then is default 0
        id = App.getAppInstance().getPreferences().getTokenId();

        RestDataRepository dataRepository = new RestDataRepository();
        dataRepository.registerToken(token, id)
                .subscribe(response -> {

                    if (response.error.isEmpty()) {
                        if (response.id != 0) {
                            App.getAppInstance().getPreferences().setTokenId(response.id);
                            App.getAppInstance().getPreferences().setToken(token);
                        }
                    }
                });
    }
}
