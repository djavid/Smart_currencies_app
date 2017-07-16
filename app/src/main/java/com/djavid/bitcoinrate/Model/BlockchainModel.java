
package com.djavid.bitcoinrate.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockchainModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("values")
    @Expose
    private List<Value> values = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public BlockchainModel() {
    }

    /**
     * 
     * @param unit
     * @param values
     * @param status
     * @param description
     * @param name
     * @param period
     */
    public BlockchainModel(String status, String name, String unit, String period, String description, List<Value> values) {
        super();
        this.status = status;
        this.name = name;
        this.unit = unit;
        this.period = period;
        this.description = description;
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

}
