package com.tokopedia.flight.search.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightFilterAirlineViewHolder;
import com.tokopedia.flight.search.view.model.resultstatistics.AirlineStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterAirlineAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<AirlineStat> {
    private CheckableBaseViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterAirlineAdapterTypeFactory(CheckableBaseViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(AirlineStat viewModel) {
        return FlightFilterAirlineViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterAirlineViewHolder.LAYOUT) {
            return new FlightFilterAirlineViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
