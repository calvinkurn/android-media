package com.tokopedia.inbox.rescenter.shipping.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ResKurirListResponse {

    @SerializedName("get_shipping_list")
    JsonObject getShippingListRespose;

    public JsonObject getGetShippingListRespose() {
        return getShippingListRespose;
    }

    public void setGetShippingListRespose(JsonObject getShippingListRespose) {
        this.getShippingListRespose = getShippingListRespose;
    }
}
