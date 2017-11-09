package com.tokopedia.flight.booking.view.adapter;

import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodeAdapter extends BaseListAdapter<FlightBookingPhoneCodeViewModel> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingPhoneCodeViewHolder(getLayoutView(parent, R.layout.item_flight_booking_phone_code));
    }
}
