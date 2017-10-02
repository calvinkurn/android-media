package com.tokopedia.gm.cashback.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 10/2/17.
 */

public class RequestCashbackModel {
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("cashback")
    private String cashback;

    public RequestCashbackModel(String product_id, String cashback) {
        this.product_id = product_id;
        this.cashback = cashback;
    }
}
