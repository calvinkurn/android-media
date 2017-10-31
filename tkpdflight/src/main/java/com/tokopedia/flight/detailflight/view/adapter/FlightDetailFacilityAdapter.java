package com.tokopedia.flight.detailflight.view.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityAdapter extends BaseListAdapter<Route> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_detail_facility);
        return new FlightDetailFacilityViewHolder(view);
    }
}
