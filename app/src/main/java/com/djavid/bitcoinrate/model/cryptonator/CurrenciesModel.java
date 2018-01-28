
package com.djavid.bitcoinrate.model.cryptonator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrenciesModel {

    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrenciesModel() {
    }

    /**
     * 
     * @param rows
     */
    public CurrenciesModel(List<Row> rows) {
        super();
        this.rows = rows;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

}
