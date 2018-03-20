package com.tokopedia.flight.passenger.data.cloud.requestbody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 20/03/18.
 */

public class UpdatePassengerRequest {

    @SerializedName("attributes")
    @Expose
    private UpdatePassengerAttributesRequest attributes;

    public UpdatePassengerRequest(UpdatePassengerAttributesRequest attributes) {
        this.attributes = attributes;
    }
}
