package com.tokopedia.flight.search.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.adapter.FlightAirportViewHolder;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchAdapter extends BaseListAdapter<FlightSearchData> {
    @Override
    public BaseViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_airport);
        FlightAirportViewHolder productManageListViewHolder = new FlightAirportViewHolder(view);
        return productManageListViewHolder;
    }
}
