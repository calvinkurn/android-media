package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerAdapterTypeFactory extends BaseAdapterTypeFactory{

    private FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    public FlightBookingListPassengerAdapterTypeFactory(FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
    }

    public int type(FlightPassengerViewModel viewModel) {
        return FlightBookingListPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightBookingListPassengerViewHolder.LAYOUT) {
            return new FlightBookingListPassengerViewHolder(parent, listenerCheckedSavedPassenger);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
