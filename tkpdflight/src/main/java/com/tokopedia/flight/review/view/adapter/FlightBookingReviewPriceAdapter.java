package com.tokopedia.flight.review.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPriceAdapter extends BaseListAdapter<SimpleViewModel> {
    public FlightBookingReviewPriceAdapter(Context context) {
        super(context, null);
    }
    public FlightBookingReviewPriceAdapter(Context context, OnBaseListV2AdapterListener<SimpleViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightBookingReviewPriceAdapter(Context context, @Nullable List<SimpleViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<SimpleViewModel> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingreviewPriceViewHolder(getLayoutView(parent, R.layout.item_flight_review_price));
    }
}
