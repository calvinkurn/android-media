package com.tokopedia.flight.booking.view.adapter;

import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseMultipleCheckListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsAdapter extends BaseMultipleCheckListAdapter<FlightBookingMealViewModel> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingMealsViewHolder(getLayoutView(parent, R.layout.item_flight_booking_meals));
    }
}
