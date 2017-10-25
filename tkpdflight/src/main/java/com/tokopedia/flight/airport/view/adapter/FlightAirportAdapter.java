package com.tokopedia.flight.airport.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportAdapter extends BaseListAdapter<FlightAirportDB> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FlightAirportDB.TYPE:
                FlightAirportViewHolder productManageListViewHolder = new FlightAirportViewHolder(
                        getLayoutView(parent, R.layout.item_flight_airport));
                return productManageListViewHolder;
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }
}
