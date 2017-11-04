package com.djavid.bitcoinrate.model.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class LabelItemRealm extends RealmObject {

    private String value;
    private boolean isTrendingUp;
    private boolean isAddButton;


    public LabelItemRealm() {
        this.isAddButton = true;
    }

    public LabelItemRealm(String value, boolean isTrendingUp) {
        this.value = value;
        this.isTrendingUp = isTrendingUp;
        this.isAddButton = false;
    }

    @Override
    public String toString() {
        return "LabelItemRealm{" +
                "value='" + value + '\'' +
                ", isTrendingUp=" + isTrendingUp +
                ", isAddButton=" + isAddButton +
                '}';
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isTrendingUp() {
        return isTrendingUp;
    }

    public void setTrendingUp(boolean trendingUp) {
        isTrendingUp = trendingUp;
    }

    public boolean isAddButton() {
        return isAddButton;
    }

    public void setAddButton(boolean addButton) {
        isAddButton = addButton;
    }
}
