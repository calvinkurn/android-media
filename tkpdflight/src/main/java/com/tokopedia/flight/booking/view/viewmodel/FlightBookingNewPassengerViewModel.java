package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.passenger.view.adapter.FlightPassengerListTypeFactory;

/**
 * @author by furqan on 07/03/18.
 */

public class FlightBookingNewPassengerViewModel implements Visitable<FlightPassengerListTypeFactory> {

    private String title;

    public FlightBookingNewPassengerViewModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int type(FlightPassengerListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}