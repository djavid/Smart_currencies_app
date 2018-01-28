package com.djavid.bitcoinrate.model.heroku;


public class ResponseId {

    public String error;
    public long id;


    public ResponseId(long id) {
        this.id = id;
        error = "";
    }

    public ResponseId(String error) {
        this.error = error;
    }
}
