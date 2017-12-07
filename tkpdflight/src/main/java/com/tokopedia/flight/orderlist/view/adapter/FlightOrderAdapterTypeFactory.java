package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.BaseAdapterTypeFactory;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by alvarisi on 12/7/17.
 */

public class FlightOrderAdapterTypeFactory extends BaseAdapterTypeFactory implements FlightOrderTypeFactory {
    @Override
    public AbstractViewHolder createViewHolder(View view, int viewType) {
        AbstractViewHolder viewHolder;
        switch (viewType) {
            default:
                viewHolder = super.createViewHolder(view, viewType);
        }
        return viewHolder;
    }
}
