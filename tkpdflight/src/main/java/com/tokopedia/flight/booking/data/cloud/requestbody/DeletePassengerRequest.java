package com.tokopedia.flight.booking.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 05/03/18.
 */

public class DeletePassengerRequest {
    @SerializedName("id")
    @Expose
    private String id;

    public DeletePassengerRequest(String id) {
        this.id = id;
    }

    public DeletePassengerRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
