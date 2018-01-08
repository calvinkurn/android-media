package com.tokopedia.flight.airport.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by alvarisi on 12/19/17.
 */

public class FlightAirportAdapterTypeFactory extends BaseAdapterTypeFactory {
    private FlightAirportViewHolder.FilterTextListener filterTextListener;

    public FlightAirportAdapterTypeFactory(FlightAirportViewHolder.FilterTextListener filterTextListener) {
        this.filterTextListener = filterTextListener;
    }

    public int type(FlightAirportDB viewModel) {
        return FlightAirportViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightAirportViewHolder.LAYOUT) {
            return new FlightAirportViewHolder(parent, filterTextListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
