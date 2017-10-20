package com.djavid.bitcoinrate.model.realm;

import com.djavid.bitcoinrate.model.dto.LabelItemDto;

import java.util.List;
import io.realm.RealmObject;


public class TickerItemRealm extends RealmObject {

    private String price;
    private String code_crypto;
    private String code_country;
    private List<LabelItemDto> labels;

    public TickerItemRealm() {}

    public TickerItemRealm(String price, String code_crypto, String code_country, List<LabelItemDto> labels) {
        this.price = price;
        this.code_crypto = code_crypto;
        this.code_country = code_country;
        this.labels = labels;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public List<LabelItemDto> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelItemDto> labels) {
        this.labels = labels;
    }
}
