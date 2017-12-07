package com.tokopedia.flight.orderlist.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;

/**
 * Created by alvarisi on 12/7/17.
 */

public interface FlightOrderTypeFactory {
    AbstractViewHolder createViewHolder(View view, int viewType);
}
