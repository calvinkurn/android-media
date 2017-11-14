
package com.tokopedia.transaction.purchase.detail.model.history.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
