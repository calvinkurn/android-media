package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingSavedPassengerViewHolder;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingSavedPassengerAdapterTypeFactory extends BaseAdapterTypeFactory{

    private FlightBookingSavedPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger;

    public FlightBookingSavedPassengerAdapterTypeFactory(FlightBookingSavedPassengerViewHolder.ListenerCheckedSavedPassenger listenerCheckedSavedPassenger) {
        this.listenerCheckedSavedPassenger = listenerCheckedSavedPassenger;
    }

    public int type(FlightPassengerViewModel viewModel) {
        return FlightBookingSavedPassengerViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightBookingSavedPassengerViewHolder.LAYOUT) {
            return new FlightBookingSavedPassengerViewHolder(parent, listenerCheckedSavedPassenger);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
