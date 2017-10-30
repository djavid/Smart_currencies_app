package com.djavid.bitcoinrate.model.dto;


import io.realm.RealmObject;

public class LabelItemDto extends RealmObject {

    private String value;
    private boolean isTrendingUp;
    private boolean isAddButton;


    public LabelItemDto() {
        this.isAddButton = true;
    }

    public LabelItemDto(String value, boolean isTrendingUp) {
        this.value = value;
        this.isTrendingUp = isTrendingUp;
        this.isAddButton = false;
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
