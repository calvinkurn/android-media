package com.tokopedia.seller.topads.model.exchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataDeposit;

/**
 * Created by zulfikarrahman on 11/4/16.
 */


public class DepositResponse {

    @SerializedName("data")
    @Expose
    private DataDeposit data;

    /**
     *
     * @return
     * The data
     */
    public DataDeposit getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(DataDeposit data) {
        this.data = data;
    }

}
