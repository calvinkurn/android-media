package com.tokopedia.flight.search.data.cloud.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;

/**
 * Created by User on 11/14/2017.
 */

public class FlightDataResponse<T> extends DataResponse<T> {
    @SerializedName(value="meta")
    @Expose
    private Meta meta;

    public Meta getMeta() {
        return meta;
    }
}
