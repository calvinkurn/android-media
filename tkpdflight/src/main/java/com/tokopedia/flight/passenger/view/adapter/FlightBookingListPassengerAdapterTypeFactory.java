package com.tokopedia.flight.passenger.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightBookingNewPassengerViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightBookingListPassengerTypeFactory {

    private FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;
    private FlightBookingNewPassengerViewHolder.ListenerClickedNewPassenger listenerClickedNewPassenger;

    public FlightBookingListPassengerAdapterTypeFactory(FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger,
                                                        FlightBookingNewPassengerViewHolder.ListenerClickedNewPassenger listenerClickedNewPassenger) {
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
        this.listenerClickedNewPassenger = listenerClickedNewPassenger;
    }

    @Override
    public int type(FlightBookingPassengerViewModel viewModel) {
        return FlightBookingListPassengerViewHolder.LAYOUT;
    }

    @Override
    public int type(FlightBookingNewPassengerViewModel viewModel) {
        return FlightBookingNewPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        AbstractViewHolder viewHolder = null;
        if (type == FlightBookingListPassengerViewHolder.LAYOUT) {
            viewHolder = new FlightBookingListPassengerViewHolder(parent, listenerCheckedSavedPassenger);
        } else if(type == FlightBookingNewPassengerViewHolder.LAYOUT) {
            viewHolder = new FlightBookingNewPassengerViewHolder(parent, listenerClickedNewPassenger);
        } else {
            viewHolder = super.createViewHolder(parent, type);
        }
        return viewHolder;
    }
}
