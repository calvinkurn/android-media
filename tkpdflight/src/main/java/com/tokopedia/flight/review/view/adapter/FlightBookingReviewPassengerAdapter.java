package com.tokopedia.flight.review.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.review.view.model.FlightDetailPassenger;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPassengerAdapter extends BaseListAdapter<FlightDetailPassenger> {
    public FlightBookingReviewPassengerAdapter(Context context) {
        super(context, null);
    }

    public FlightBookingReviewPassengerAdapter(Context context, OnBaseListV2AdapterListener<FlightDetailPassenger> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightBookingReviewPassengerAdapter(Context context, @Nullable List<FlightDetailPassenger> data, int rowPerPage, OnBaseListV2AdapterListener<FlightDetailPassenger> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingReviewPassengerViewHolder(getLayoutView(parent, R.layout.item_flight_review_passenger));
    }
}
