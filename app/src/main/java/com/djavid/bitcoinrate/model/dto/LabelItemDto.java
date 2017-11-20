package com.djavid.bitcoinrate.model.dto;


public class LabelItemDto {

    private Long id;
    private String value;
    private boolean isTrendingUp;
    private boolean isAddButton;


    public LabelItemDto() {
        this.isAddButton = true;
    }

    public LabelItemDto(long id, String value, boolean isTrendingUp, boolean isAddButton) {
        this.id = id;
        this.value = value;
        this.isTrendingUp = isTrendingUp;
        this.isAddButton = isAddButton;
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

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
