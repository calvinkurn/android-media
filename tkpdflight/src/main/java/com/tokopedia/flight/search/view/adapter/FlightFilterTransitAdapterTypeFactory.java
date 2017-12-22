package com.tokopedia.flight.search.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.BaseListCheckableTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.holder.CheckableBaseViewHolder;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.search.view.adapter.viewholder.FlightFilterTransitViewHolder;
import com.tokopedia.flight.search.view.model.resultstatistics.TransitStat;

/**
 * Created by alvarisi on 12/21/17.
 */

public class FlightFilterTransitAdapterTypeFactory extends BaseAdapterTypeFactory implements BaseListCheckableTypeFactory<TransitStat> {
    private CheckableBaseViewHolder.CheckableInteractionListener interactionListener;

    public FlightFilterTransitAdapterTypeFactory(CheckableBaseViewHolder.CheckableInteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public int type(TransitStat viewModel) {
        return FlightFilterTransitViewHolder.LAYOUT;
    }

    @Override
    public AbstractViewHolder createViewHolder(View parent, int type) {
        if (type == FlightFilterTransitViewHolder.LAYOUT) {
            return new FlightFilterTransitViewHolder(parent, interactionListener);
        } else {
            return super.createViewHolder(parent, type);
        }
    }
}
