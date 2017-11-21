package com.tokopedia.flight.review;

import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPriceAdapter extends BaseListAdapter<FlightSearchViewModel> {
    public FlightBookingReviewPriceAdapter() {
        super(null);
    }
    public FlightBookingReviewPriceAdapter(OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(onBaseListV2AdapterListener);
    }

    public FlightBookingReviewPriceAdapter(@Nullable List<FlightSearchViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightSearchViewModel> onBaseListV2AdapterListener) {
        super(data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingreviewPriceViewHolder(getLayoutView(parent, R.layout.item_flight_review_price));
    }
}
