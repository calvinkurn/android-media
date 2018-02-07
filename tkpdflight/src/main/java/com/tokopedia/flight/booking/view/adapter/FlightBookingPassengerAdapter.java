package com.tokopedia.flight.booking.view.adapter;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter;
import com.tokopedia.abstraction.base.view.adapter.Visitable;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingPassengerAdapter extends BaseAdapter<FlightBookingPassengerAdapterTypeFactory> {

    public FlightBookingPassengerAdapter(FlightBookingPassengerAdapterTypeFactory adapterTypeFactory, List<Visitable> visitables) {
        super(adapterTypeFactory, visitables);
    }

}
