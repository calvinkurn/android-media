package com.tokopedia.flight.booking.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 22/02/18.
 */

public class SavedPassengerEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private PassengerAttribute passengerAttribute;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PassengerAttribute getPassengerAttribute() {
        return passengerAttribute;
    }

    public void setPassengerAttribute(PassengerAttribute passengerAttribute) {
        this.passengerAttribute = passengerAttribute;
    }
}
