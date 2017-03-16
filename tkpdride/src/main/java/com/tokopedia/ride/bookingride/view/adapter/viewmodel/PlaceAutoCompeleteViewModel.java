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
}
