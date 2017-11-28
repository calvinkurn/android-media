package com.tokopedia.flight.common.data.model;

import android.support.annotation.StringDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.common.constant.FlightErrorConstant;

/**
 * Created by User on 11/28/2017.
 */

public class FlightError {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;

    public @FlightErrorConstant String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
