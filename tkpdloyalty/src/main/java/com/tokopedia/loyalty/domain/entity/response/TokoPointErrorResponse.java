package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 13/12/17.
 */

public class TokoPointErrorResponse {
    @SerializedName("header")
    @Expose
    private TokoPointHeaderResponse headerResponse;

    public TokoPointHeaderResponse getHeaderResponse() {
        return headerResponse;
    }
}
