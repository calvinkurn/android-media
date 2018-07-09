package com.tokopedia.loyalty.domain.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
