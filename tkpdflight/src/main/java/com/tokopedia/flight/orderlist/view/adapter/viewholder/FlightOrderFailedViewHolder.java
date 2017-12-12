package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderFailedViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderFailedViewHolder extends AbstractViewHolder<FlightOrderFailedViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_failed;

    public FlightOrderFailedViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(FlightOrderFailedViewModel element) {

    }
}