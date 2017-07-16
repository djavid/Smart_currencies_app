
package com.djavid.bitcoinrate.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Row {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("statuses")
    @Expose
    private List<String> statuses = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Row() {
    }

    /**
     * 
     * @param name
     * @param code
     * @param statuses
     */
    public Row(String code, String name, List<String> statuses) {
        super();
        this.code = code;
        this.name = name;
        this.statuses = statuses;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<String> statuses) {
        this.statuses = statuses;
    }

}
