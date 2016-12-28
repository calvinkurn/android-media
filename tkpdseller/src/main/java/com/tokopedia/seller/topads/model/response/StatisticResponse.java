package com.tokopedia.seller.topads.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataStatistic;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class StatisticResponse {

    @SerializedName("data")
    @Expose
    private DataStatistic data;

    /**
     *
     * @return
     * The data
     */
    public DataStatistic getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(DataStatistic data) {
        this.data = data;
    }

}
