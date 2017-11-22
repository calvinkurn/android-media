package com.tokopedia.flight.airport.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.holder.BaseViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

import java.util.List;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportAdapter extends BaseListAdapter<FlightAirportDB> {

    public FlightAirportAdapter(Context context, OnBaseListV2AdapterListener<FlightAirportDB> onBaseListV2AdapterListener) {
        super(context, onBaseListV2AdapterListener);
    }

    public FlightAirportAdapter(Context context, @Nullable List<FlightAirportDB> data, int rowPerPage, OnBaseListV2AdapterListener<FlightAirportDB> onBaseListV2AdapterListener) {
        super(context, data, rowPerPage, onBaseListV2AdapterListener);
    }

    @Override
    public BaseViewHolder<FlightAirportDB> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutView(parent, R.layout.item_flight_airport);
        FlightAirportViewHolder productManageListViewHolder = new FlightAirportViewHolder(view);
        return productManageListViewHolder;
    }
}
