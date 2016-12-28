package com.tokopedia.seller.topads.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataResponseActionAds;

/**
 * Created by zulfikarrahman on 12/14/16.
 */
public class ActionAdsResponse {
    @SerializedName("data")
    @Expose
    private DataResponseActionAds data;

    public DataResponseActionAds getData() {
        return data;
    }

    public void setData(DataResponseActionAds data) {
        this.data = data;
    }
}
