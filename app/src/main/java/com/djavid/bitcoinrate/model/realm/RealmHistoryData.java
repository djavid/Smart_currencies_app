package com.djavid.bitcoinrate.model.realm;

import com.djavid.bitcoinrate.model.cryptocompare.Datum;
import com.djavid.bitcoinrate.model.project.ChartOption;

import io.realm.RealmList;
import io.realm.RealmObject;


public class RealmHistoryData extends RealmObject {

    private String pair;
    private ChartOption option;
    private RealmList<Datum> values = null;


    public RealmHistoryData() { }

    public RealmHistoryData(RealmList<Datum> values, String pair, ChartOption option) {
        this.pair = pair;
        this.values = values;
        this.option = option;
    }

    @Override
    public String toString() {
        return "RealmHistoryData{" +
                "values=" + values +
                "option=" + option.timespan +
                '}';
    }


    public RealmList<Datum> getValues() {
        if (values == null) return new RealmList<>();

        return values;
    }
    public void setValues(RealmList<Datum> values) {
        this.values = values;
    }

    public String getPair() {
        return pair;
    }
    public void setPair(String pair) {
        this.pair = pair;
    }

    public ChartOption getOption() {
        return option;
    }
    public void setOption(ChartOption option) {
        this.option = option;
    }

}
