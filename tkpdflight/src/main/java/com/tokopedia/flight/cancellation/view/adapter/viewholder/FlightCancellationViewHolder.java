package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.FlightCancellationViewModel;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationViewHolder extends AbstractViewHolder<FlightCancellationViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_cancellation;

    public FlightCancellationViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(FlightCancellationViewModel element) {

    }
}
