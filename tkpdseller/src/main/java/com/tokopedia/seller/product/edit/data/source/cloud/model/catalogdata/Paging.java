package com.tokopedia.seller.product.edit.data.source.cloud.model.catalogdata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendry on 4/5/2017.
 */

public class Paging {

    @SerializedName("uri_next")
    @Expose
    private String uriNext;
    @SerializedName("uri_previous")
    @Expose
    private String uriPrevious;

    public String getUriNext() {
        return uriNext;
    }

    public void setUriNext(String uriNext) {
        this.uriNext = uriNext;
    }

    public String getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(String uriPrevious) {
        this.uriPrevious = uriPrevious;
    }

}