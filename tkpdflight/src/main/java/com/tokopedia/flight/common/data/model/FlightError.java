package com.tokopedia.flight.common.data.model;

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

    public FlightError(String id) {
        this.id = id;
    }

    public FlightError() {
    }

    public @FlightErrorConstant
    String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FlightError && ((FlightError) obj).getId().equalsIgnoreCase(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return title;
    }
}
