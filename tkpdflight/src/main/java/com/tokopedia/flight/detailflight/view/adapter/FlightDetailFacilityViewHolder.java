package com.tokopedia.flight.detailflight.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.search.data.cloud.model.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityViewHolder extends BaseViewHolder<Route> {

    RecyclerView listInfo;
    GridView gridAmenity;

    public FlightDetailFacilityViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindObject(Route route) {

    }
}
