package com.tokopedia.inbox.rescenter.createreso.data.pojo.solution;

import com.google.gson.annotations.SerializedName;

/**
 * @author by yfsx on 08/08/18.
 */
public class SolutionProblemAmountResponse {

    @SerializedName("idr")
    private String idr;
    @SerializedName("integer")
    private int integer;

    public String getIdr() {
        return idr;
    }

    public void setIdr(String idr) {
        this.idr = idr;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }
}
