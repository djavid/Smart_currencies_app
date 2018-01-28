
package com.djavid.bitcoinrate.model.blockchain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("x")
    @Expose
    private long x;
    @SerializedName("y")
    @Expose
    private double y;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Value() {
    }

    /**
     * 
     * @param y
     * @param x
     */
    public Value(long x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
