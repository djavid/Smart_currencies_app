package com.djavid.bitcoinrate.model.dto;


public class LabelItemDto {

    private Long id;
    private String value;
    private boolean isTrendingUp;
    private boolean isAddButton;
    private double change_percent;
    private boolean isPercentLabel;


    public LabelItemDto() {
        this.isAddButton = true;
    }

    public LabelItemDto(long id, String value, boolean isTrendingUp, double change_percent) {
        this.id = id;
        this.value = value;
        this.isTrendingUp = isTrendingUp;
        this.change_percent = change_percent;
        this.isAddButton = false;

        if (change_percent > 0) isPercentLabel = true;
    }

    public LabelItemDto(String value, boolean isTrendingUp, boolean isPercentLabel) {
        this.isTrendingUp = isTrendingUp;
        this.isPercentLabel = isPercentLabel;

        if (isPercentLabel) {
            this.change_percent = Double.parseDouble(value);
        } else {
            this.value = value;
        }
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

    public boolean isPercentLabel() {
        return isPercentLabel;
    }
    public void setPercentLabel(boolean percentLabel) {
        isPercentLabel = percentLabel;
    }

    public double getChange_percent() {
        return change_percent;
    }
    public void setChange_percent(double change_percent) {
        this.change_percent = change_percent;
    }
}
