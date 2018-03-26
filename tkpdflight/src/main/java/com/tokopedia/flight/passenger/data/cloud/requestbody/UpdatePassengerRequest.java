package com.tokopedia.flight.passenger.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 20/03/18.
 */

public class UpdatePassengerRequest {

    @SerializedName("id")
    @Expose
    private String passengerId;
    @SerializedName("attributes")
    @Expose
    private UpdatePassengerAttributesRequest attributes;

    public UpdatePassengerRequest(String passengerId, UpdatePassengerAttributesRequest attributes) {
        this.passengerId = passengerId;
        this.attributes = attributes;
    }

    public UpdatePassengerAttributesRequest getAttributes() {
        return attributes;
    }

    public void setAttributes(UpdatePassengerAttributesRequest attributes) {
        this.attributes = attributes;
    }
}
