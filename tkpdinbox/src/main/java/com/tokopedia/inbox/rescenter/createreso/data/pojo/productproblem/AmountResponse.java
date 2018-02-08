package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class AmountResponse {
    @SerializedName("idr")
    @Expose
    private String idr;
    @SerializedName("integer")
    @Expose
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

    @Override
    public String toString() {
        return "AmountResponse{" +
                "idr='" + idr + '\'' +
                ", integer='" + integer + '\'' +
                '}';
    }
}
