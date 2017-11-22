package com.tokopedia.flight.booking.view.adapter;

import android.content.Context;
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

public class FlightBookingPhoneCodeAdapter extends BaseListAdapter<FlightBookingPhoneCodeViewModel> {
    public FlightBookingPhoneCodeAdapter(Context context, OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightBookingPhoneCodeAdapter(Context context, @Nullable List<FlightBookingPhoneCodeViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightBookingPhoneCodeViewModel> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingPhoneCodeViewHolder(getLayoutView(parent, R.layout.item_flight_booking_phone_code));
    }
}
