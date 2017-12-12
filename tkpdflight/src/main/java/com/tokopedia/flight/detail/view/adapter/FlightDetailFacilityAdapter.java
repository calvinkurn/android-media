package com.tokopedia.flight.detail.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.model.FlightDetailRouteViewModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityAdapter extends BaseListAdapter<FlightDetailRouteViewModel> {
    public FlightDetailFacilityAdapter(Context context, OnBaseListV2AdapterListener<FlightDetailRouteViewModel> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightDetailFacilityAdapter(Context context, @Nullable List<FlightDetailRouteViewModel> data, int rowPerPage, OnBaseListV2AdapterListener<FlightDetailRouteViewModel> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_detail_facility);
        return new FlightDetailFacilityViewHolder(view);
    }
}
