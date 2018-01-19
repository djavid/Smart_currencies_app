package com.djavid.bitcoinrate.model.dto.cryptocompare;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class Datum extends RealmObject {

    @SerializedName("time")
    @Expose
    private Long time;

    @SerializedName("open")
    @Expose
    private Double open;

    @SerializedName("close")
    @Expose
    private Double close;

    @SerializedName("high")
    @Expose
    private Double high;

    @SerializedName("low")
    @Expose
    private Double low;

    @SerializedName("volumefrom")
    @Expose
    private Double volumefrom;

    @SerializedName("volumeto")
    @Expose
    private Double volumeto;


    public Datum() {}

    public Datum(Long time, Double open, Double close, Double high, Double low, Double volumefrom, Double volumeto) {
        this.time = time;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volumefrom = volumefrom;
        this.volumeto = volumeto;
    }

    @Override
    public String toString() {
        return "Datum{" +
                "time=" + time +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volumefrom=" + volumefrom +
                ", volumeto=" + volumeto +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Datum datum = (Datum) o;

        if (time != null ? !time.equals(datum.time) : datum.time != null) return false;
        if (open != null ? !open.equals(datum.open) : datum.open != null) return false;
        if (close != null ? !close.equals(datum.close) : datum.close != null) return false;
        if (high != null ? !high.equals(datum.high) : datum.high != null) return false;
        if (low != null ? !low.equals(datum.low) : datum.low != null) return false;
        if (volumefrom != null ? !volumefrom.equals(datum.volumefrom) : datum.volumefrom != null)
            return false;
        return volumeto != null ? volumeto.equals(datum.volumeto) : datum.volumeto == null;
    }

    @Override
    public int hashCode() {
        int result = time != null ? time.hashCode() : 0;
        result = 31 * result + (open != null ? open.hashCode() : 0);
        result = 31 * result + (close != null ? close.hashCode() : 0);
        result = 31 * result + (high != null ? high.hashCode() : 0);
        result = 31 * result + (low != null ? low.hashCode() : 0);
        result = 31 * result + (volumefrom != null ? volumefrom.hashCode() : 0);
        result = 31 * result + (volumeto != null ? volumeto.hashCode() : 0);
        return result;
    }


    public Long getTime() {
        return time;
    }
    public void setTime(Long time) {
        this.time = time;
    }

    public Double getOpen() {
        return open;
    }
    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }
    public void setClose(Double close) {
        this.close = close;
    }

    public Double getHigh() {
        return high;
    }
    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }
    public void setLow(Double low) {
        this.low = low;
    }

    public Double getVolumefrom() {
        return volumefrom;
    }
    public void setVolumefrom(Double volumefrom) {
        this.volumefrom = volumefrom;
    }

    public Double getVolumeto() {
        return volumeto;
    }
    public void setVolumeto(Double volumeto) {
        this.volumeto = volumeto;
    }
}
