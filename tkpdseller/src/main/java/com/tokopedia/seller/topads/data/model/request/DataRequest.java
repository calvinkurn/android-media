package com.tokopedia.seller.topads.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.keyword.data.model.cloud.bulkkeyword.DataBulkKeyword;

/**
 * Created by zulfikarrahman on 12/14/16.
 */

public class DataRequest<T> {
    @SerializedName("data")
    @Expose
    T data;

    public DataRequest(){

    }

    public DataRequest(T dataBulkKeyword) {
        this.data = dataBulkKeyword;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
