package com.tokopedia.flight.booking.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightBookingPassengerTypeFactory {

    int type(FlightBookingPassengerViewModel viewModel);

    AbstractViewHolder createViewHolder(View view, int viewType);
}
