package com.tokopedia.gm.cashback.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/4/17.
 */

public class RequestGetCashbackModel {
    @SerializedName("data")
    private List<DataRequestGetCashback> data;

    public RequestGetCashbackModel(List<DataRequestGetCashback> data) {
        this.data = data;
    }

    public static class DataRequestGetCashback {
        @SerializedName("product_id")
        private String product_id;
        @SerializedName("shop_id")
        private String cashback;

        public DataRequestGetCashback(String product_id, String cashback) {
            this.product_id = product_id;
            this.cashback = cashback;
        }
    }
}
