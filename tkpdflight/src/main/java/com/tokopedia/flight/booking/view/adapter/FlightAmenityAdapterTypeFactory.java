package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingAmenityViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightAmenityAdapterTypeFactory extends BaseListAdapterTypeFactory<FlightBookingAmenityViewModel> {
    private FlightBookingAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass;

    public FlightAmenityAdapterTypeFactory(FlightBookingAmenityViewHolder.ListenerCheckedLuggage listenerCheckedClass) {
        this.listenerCheckedClass = listenerCheckedClass;
    }

    @Override
    public int type(FlightBookingAmenityViewModel viewModel) {
        return FlightBookingAmenityViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightBookingAmenityViewHolder.LAYOUT) {
            return new FlightBookingAmenityViewHolder(parent, listenerCheckedClass);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
