package com.tokopedia.loyalty.domain.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.loyalty.domain.entity.response.TokoPointDrawerDataResponse;

/**
 * Created by sachinbansal on 5/15/18.
 */

public class GqlTokoPointResponse {

    @SerializedName("data")
    @Expose
    private HachikoDrawerDataResponse hachikoDrawerDataResponse;


    public HachikoDrawerDataResponse getHachikoDrawerDataResponse() {
        return hachikoDrawerDataResponse;
    }

    public void setHachikoDrawerDataResponse(HachikoDrawerDataResponse hachikoDrawerDataResponse) {
        this.hachikoDrawerDataResponse = hachikoDrawerDataResponse;
    }

}
