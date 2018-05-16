package com.tokopedia.loyalty.domain.repository;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachinbansal on 5/15/18.
 */

public class HachikoDrawerDataResponse {

    @SerializedName("tokopoints")
    @Expose
    private GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse;


    public GqlTokoPointDrawerDataResponse getGqlTokoPointDrawerDataResponse() {
        return gqlTokoPointDrawerDataResponse;
    }

    public void setGqlTokoPointDrawerDataResponse(GqlTokoPointDrawerDataResponse gqlTokoPointDrawerDataResponse) {
        this.gqlTokoPointDrawerDataResponse = gqlTokoPointDrawerDataResponse;
    }
}
