package com.djavid.bitcoinrate.model.dto.cryptocompare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class HistoData {

    @SerializedName("Response")
    @Expose
    public String response;

    @SerializedName("Message")
    @Expose
    public String message;

    @SerializedName("Type")
    @Expose
    public Integer type;

    @SerializedName("Aggregated")
    @Expose
    public Boolean aggregated;

    @SerializedName("Data")
    @Expose
    public List<Datum> data = null;

    @SerializedName("TimeTo")
    @Expose
    public long timeTo;

    @SerializedName("TimeFrom")
    @Expose
    public long timeFrom;

    @SerializedName("ConversionType")
    @Expose
    public ConversionType conversionType;


    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getAggregated() {
        return aggregated;
    }
    public void setAggregated(Boolean aggregated) {
        this.aggregated = aggregated;
    }

    public List<Datum> getData() {
        return data;
    }
    public void setData(List<Datum> data) {
        this.data = data;
    }

    public long getTimeTo() {
        return timeTo;
    }
    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }

    public long getTimeFrom() {
        return timeFrom;
    }
    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public ConversionType getConversionType() {
        return conversionType;
    }
    public void setConversionType(ConversionType conversionType) {
        this.conversionType = conversionType;
    }

}
