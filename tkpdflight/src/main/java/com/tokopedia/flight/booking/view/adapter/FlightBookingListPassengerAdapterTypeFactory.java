package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightBookingPassengerTypeFactory {

    private FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    public FlightBookingListPassengerAdapterTypeFactory(FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
    }

    @Override
    public int type(FlightBookingPassengerViewModel viewModel) {
        return FlightBookingListPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightBookingListPassengerViewHolder.LAYOUT) {
            viewHolder = new FlightBookingListPassengerViewHolder(parent, listenerCheckedSavedPassenger);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
