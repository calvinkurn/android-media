package com.tokopedia.flight.orderlist.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alvarisi on 12/6/17.
 */

public class PassengerAmentityEntity {
    @SerializedName("departure_id")
    @Expose
    private String departureAirportId;
    @SerializedName("arrival_id")
    @Expose
    private String arrivalAirportId;
    @SerializedName("amenity_type")
    @Expose
    private String amenityType;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("sequence")
    @Expose
    private int sequence;

    public String getDepartureAirportId() {
        return departureAirportId;
    }

    public String getArrivalAirportId() {
        return arrivalAirportId;
    }

    public String getAmenityType() {
        return amenityType;
    }

    public String getPrice() {
        return price;
    }

    public String getDetail() {
        return detail;
    }

    public int getSequence() {
        return sequence;
    }
}
