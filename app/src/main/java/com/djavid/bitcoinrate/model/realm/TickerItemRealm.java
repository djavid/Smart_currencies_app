package com.djavid.bitcoinrate.model.realm;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class TickerItemRealm extends RealmObject {


    private Date createdAt = new Date();
    private String code_crypto;
    private String code_country;
    private RealmList<LabelItemRealm> labels;

    public TickerItemRealm() {}

    public TickerItemRealm(String code_crypto, String code_country, List<LabelItemRealm> labels) {
        this.code_crypto = code_crypto;
        this.code_country = code_country;

        this.labels = new RealmList<>();
        this.labels.addAll(labels);
    }

    public TickerItemRealm(String code_crypto, String code_country) {
        this.code_crypto = code_crypto;
        this.code_country = code_country;
        this.labels = new RealmList<>();
    }

    @Override
    public String toString() {
        return "TickerItemRealm{" +
                "createdAt=" + createdAt +
                ", code_crypto='" + code_crypto + '\'' +
                ", code_country='" + code_country + '\'' +
                ", labels=" + labels +
                '}';
    }

    public String getCode_crypto() {
        return code_crypto;
    }
    public void setCode_crypto(String code_crypto) {
        this.code_crypto = code_crypto;
    }

    public String getCode_country() {
        return code_country;
    }
    public void setCode_country(String code_country) {
        this.code_country = code_country;
    }

    public List<LabelItemRealm> getLabels() {
        return labels;
    }
    public void setLabels(List<LabelItemRealm> labels) {
        this.labels.addAll(labels);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
