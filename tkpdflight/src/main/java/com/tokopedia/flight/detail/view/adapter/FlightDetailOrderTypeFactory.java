package com.tokopedia.flight.detail.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;

/**
 * Created by alvarisi on 12/27/17.
 */

public class FlightDetailOrderTypeFactory extends BaseAdapterTypeFactory {
    public FlightDetailOrderTypeFactory() {
    }

    public int type(FlightOrderJourney flightOrderJourney) {
        return FlightDetailOrderViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightDetailOrderViewHolder.LAYOUT)
            return new FlightDetailOrderViewHolder(parent);
        else
            return super.createViewHolder(parent, type);
    }
}
