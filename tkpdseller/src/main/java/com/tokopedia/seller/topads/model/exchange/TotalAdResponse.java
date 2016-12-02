package com.tokopedia.seller.topads.model.exchange;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.TotalAd;

/**
 * Created by zulfikarrahman on 11/4/16.
 */


public class TotalAdResponse {

    @SerializedName("data")
    @Expose
    private TotalAd data;

    public TotalAd getData() {
        return data;
    }

    public void setData(TotalAd data) {
        this.data = data;
    }
}
