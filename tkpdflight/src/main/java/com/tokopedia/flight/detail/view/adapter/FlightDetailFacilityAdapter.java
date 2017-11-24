package com.tokopedia.flight.detail.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.data.cloud.model.response.Route;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityAdapter extends BaseListAdapter<Route> {
    public FlightDetailFacilityAdapter(Context context, OnBaseListV2AdapterListener<Route> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightDetailFacilityAdapter(Context context, @Nullable List<Route> data, int rowPerPage, OnBaseListV2AdapterListener<Route> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_detail_facility);
        return new FlightDetailFacilityViewHolder(view);
    }
}
