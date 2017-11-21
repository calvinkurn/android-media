package com.tokopedia.flight.booking.view.adapter;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityAdapter extends BaseListAdapter<FlightBookingPhoneCodeViewModel> {
    public FlightBookingNationalityAdapter(OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightBookingNationalityAdapter(@Nullable List<FlightBookingPhoneCodeViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingNationalityViewHolder(getLayoutView(parent, R.layout.item_flight_booking_nationality));
    }
}
