package com.djavid.bitcoinrate.model.project;

import io.realm.RealmObject;


public class ChartOption extends RealmObject {

    public String timespan;
    public int days;
    public int intervals;

    public int limit;
    public int periods;
    public String request;


    public ChartOption() { }

    public ChartOption(String timespan, int days, int intervals,
                       int limit, int periods, String request) {
        this.timespan = timespan;
        this.days = days;
        this.intervals = intervals;
        this.limit = limit;
        this.periods = periods;
        this.request = request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        ChartOption that = (ChartOption) o;

        if (days != that.days) return false;
        if (intervals != that.intervals) return false;
        if (limit != that.limit) return false;
        if (periods != that.periods) return false;
        if (timespan != null ? !timespan.equals(that.timespan) : that.timespan != null)
            return false;
        return request != null ? request.equals(that.request) : that.request == null;
    }

    @Override
    public int hashCode() {
        int result = timespan != null ? timespan.hashCode() : 0;
        result = 31 * result + days;
        result = 31 * result + intervals;
        result = 31 * result + limit;
        result = 31 * result + periods;
        result = 31 * result + (request != null ? request.hashCode() : 0);
        return result;
    }
}
