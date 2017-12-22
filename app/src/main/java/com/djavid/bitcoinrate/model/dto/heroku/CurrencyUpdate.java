package com.djavid.bitcoinrate.model.dto.heroku;

import io.realm.RealmObject;


public class CurrencyUpdate extends RealmObject {

    private long id;
    private String cryptoId;
    private String countryId;
    private double price;

    private int rank;
    private double market_cap_usd;
    private double available_supply;
    private double total_supply;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;
    private long last_updated;


    @Override
    public String toString() {
        return "CurrencyUpdate{" +
                "id=" + id +
                ", cryptoId='" + cryptoId + '\'' +
                ", countryId='" + countryId + '\'' +
                ", price=" + price +
                ", rank=" + rank +
                ", market_cap_usd=" + market_cap_usd +
                ", available_supply=" + available_supply +
                ", total_supply=" + total_supply +
                ", percent_change_1h=" + percent_change_1h +
                ", percent_change_24h=" + percent_change_24h +
                ", percent_change_7d=" + percent_change_7d +
                ", last_updated=" + last_updated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        CurrencyUpdate that = (CurrencyUpdate) o;

        if (id != that.id) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (rank != that.rank) return false;
        if (Double.compare(that.market_cap_usd, market_cap_usd) != 0) return false;
        if (Double.compare(that.available_supply, available_supply) != 0) return false;
        if (Double.compare(that.total_supply, total_supply) != 0) return false;
        if (Double.compare(that.percent_change_1h, percent_change_1h) != 0) return false;
        if (Double.compare(that.percent_change_24h, percent_change_24h) != 0) return false;
        if (Double.compare(that.percent_change_7d, percent_change_7d) != 0) return false;
        if (last_updated != that.last_updated) return false;
        if (cryptoId != null ? !cryptoId.equals(that.cryptoId) : that.cryptoId != null)
            return false;
        return countryId != null ? countryId.equals(that.countryId) : that.countryId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (cryptoId != null ? cryptoId.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + rank;
        temp = Double.doubleToLongBits(market_cap_usd);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(available_supply);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(total_supply);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(percent_change_1h);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(percent_change_24h);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(percent_change_7d);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (last_updated ^ (last_updated >>> 32));
        return result;
    }


    public double getPercentChange(String setting) {
        switch (setting) {
            case "hour":
                return getPercent_change_1h();
            case "day":
                return getPercent_change_24h();
            case "week":
                return getPercent_change_7d();
            default:
                return getPercent_change_24h();
        }
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getCryptoId() {
        return cryptoId;
    }
    public void setCryptoId(String cryptoId) {
        this.cryptoId = cryptoId;
    }

    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public int getRank() {
        return rank;
    }
    public void setRank(int rank) {
        this.rank = rank;
    }

    public double getMarket_cap_usd() {
        return market_cap_usd;
    }
    public void setMarket_cap_usd(double market_cap_usd) {
        this.market_cap_usd = market_cap_usd;
    }

    public double getAvailable_supply() {
        return available_supply;
    }
    public void setAvailable_supply(double available_supply) {
        this.available_supply = available_supply;
    }

    public double getTotal_supply() {
        return total_supply;
    }
    public void setTotal_supply(double total_supply) {
        this.total_supply = total_supply;
    }

    public double getPercent_change_1h() {
        return percent_change_1h;
    }
    public void setPercent_change_1h(double percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public double getPercent_change_24h() {
        return percent_change_24h;
    }
    public void setPercent_change_24h(double percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public double getPercent_change_7d() {
        return percent_change_7d;
    }
    public void setPercent_change_7d(double percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    public long getLast_updated() {
        return last_updated;
    }
    public void setLast_updated(long last_updated) {
        this.last_updated = last_updated;
    }
}
