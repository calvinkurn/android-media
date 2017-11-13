package com.tokopedia.flight.review;

import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 11/10/17.
 */

public class FlightBookingReviewPriceAdapter extends BaseListAdapter<FlightSearchViewModel>{
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new FlightBookingreviewPriceViewHolder(getLayoutView(parent, R.layout.item_flight_review_price));
    }
}
