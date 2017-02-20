package com.tokopedia.seller.topads.domain.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class DataRequest<T> {
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
