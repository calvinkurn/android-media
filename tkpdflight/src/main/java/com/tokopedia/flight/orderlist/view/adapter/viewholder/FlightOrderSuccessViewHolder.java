package com.tokopedia.flight.orderlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderSuccessViewModel;

/**
 * @author by alvarisi on 12/12/17.
 */

public class FlightOrderSuccessViewHolder extends AbstractViewHolder<FlightOrderSuccessViewModel> {
    @LayoutRes
    public static final int LAYOUT = R.layout.item_flight_order_success;

    public FlightOrderSuccessViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(FlightOrderSuccessViewModel element) {

    }
}
