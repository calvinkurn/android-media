package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.PlaceAutoCompleteTypeFactory;

/**
 * Created by alvarisi on 3/15/17.
 */

public class PlaceAutoCompeleteViewModel implements Visitable<PlaceAutoCompleteTypeFactory> {
    private String title;
    private String addressId;
    private String address;
    private double latitude;
    private double longitude;
    private TYPE type;
    private String distance;
    private String duration;

    public PlaceAutoCompeleteViewModel() {
    }

    @Override
    public int type(PlaceAutoCompleteTypeFactory placeAutoCompleteTypeFactory) {
        return placeAutoCompleteTypeFactory.type(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public enum TYPE {
        MARKETPLACE_PLACE,
        GOOGLE_PLACE,
        NEARBY_PLACE
    }
}
