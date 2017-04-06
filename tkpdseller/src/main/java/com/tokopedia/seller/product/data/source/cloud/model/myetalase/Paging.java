
package com.tokopedia.seller.product.data.source.cloud.model.myetalase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Paging {

    @SerializedName("uri_next")
    @Expose
    private Integer uriNext;
    @SerializedName("uri_previous")
    @Expose
    private Integer uriPrevious;

    public Integer getUriNext() {
        return uriNext;
    }

    public void setUriNext(Integer uriNext) {
        this.uriNext = uriNext;
    }

    public Integer getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(Integer uriPrevious) {
        this.uriPrevious = uriPrevious;
    }

}
