package com.tokopedia.ride.common.ride.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alvarisi on 6/14/17.
 */

public class CancelReasonsResponseEntity {
    @SerializedName("reasons")
    @Expose
    private List<String> reasons;

    public List<String> getReasons() {
        return reasons;
    }
}
