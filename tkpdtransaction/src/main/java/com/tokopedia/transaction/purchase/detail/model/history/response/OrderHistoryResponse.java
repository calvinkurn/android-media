
package com.tokopedia.transaction.purchase.detail.model.history.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transaction.purchase.detail.model.OrderDetailResponseError;

import java.util.List;

public class OrderHistoryResponse {

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("errors")
    @Expose
    private List<OrderDetailResponseError> errorList;

    public Data getData() {
        return data;
    }

    public List<OrderDetailResponseError> getErrorList() {
        return errorList;
    }
}
