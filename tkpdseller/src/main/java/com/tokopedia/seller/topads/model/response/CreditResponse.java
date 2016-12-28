package com.tokopedia.seller.topads.model.response;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataCredit;

import java.util.ArrayList;
import java.util.List;

public class CreditResponse {

    @SerializedName("data")
    @Expose
    private List<DataCredit> data = new ArrayList<DataCredit>();

    /**
     *
     * @return
     * The data
     */
    public List<DataCredit> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<DataCredit> data) {
        this.data = data;
    }

}
