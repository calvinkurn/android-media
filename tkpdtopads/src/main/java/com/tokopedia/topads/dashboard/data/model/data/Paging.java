package com.tokopedia.topads.dashboard.data.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hendry on 2/23/2017.
 */

public class Paging {

    @SerializedName("uri_previous")
    @Expose
    private Integer uriPrevious;
    @SerializedName("uri_next")
    @Expose
    private Integer uriNext;

    public Integer getUriPrevious() {
        return uriPrevious;
    }

    public void setUriPrevious(Integer uriPrevious) {
        this.uriPrevious = uriPrevious;
    }

    public Integer getUriNext() {
        return uriNext;
    }

    public void setUriNext(Integer uriNext) {
        this.uriNext = uriNext;
    }

}
