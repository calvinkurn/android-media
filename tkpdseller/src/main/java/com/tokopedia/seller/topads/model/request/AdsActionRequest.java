package com.tokopedia.seller.topads.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.model.data.DataRequestGroupAd;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class AdsActionRequest<T> {
    @SerializedName("data")
    @Expose
    T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
