package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingPhoneCodeViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightBookingPhoneCodeAdapterTypeFactory extends BaseAdapterTypeFactory {
    public FlightBookingPhoneCodeAdapterTypeFactory() {
    }

    public int type(FlightBookingPhoneCodeViewModel viewModel) {
        return FlightBookingPhoneCodeViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (FlightBookingPhoneCodeViewHolder.LAYOUT == type) {
            return new FlightBookingPhoneCodeViewHolder(parent);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
