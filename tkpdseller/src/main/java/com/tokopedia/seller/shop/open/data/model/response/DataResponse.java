package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.shop.common.exception.model.Header;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class DataResponse<T> {

    @SerializedName("header")
    @Expose
    private Header header;
    @SerializedName(value="data")
    @Expose
    private T data;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }
  
    public void setData(T data) {
        this.data = data;
    }

}
