package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationViewHolder extends AbstractViewHolder<FlightCancellationViewModel> {

    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_cancellation;

    private Context context;
    private TextView txtDepartureDetail;
    private TextView txtJourneyDetail;
    private TextView txtAirlineName;
    private TextView txtDuration;

    public FlightCancellationViewHolder(View itemView) {
        super(itemView);

        txtDepartureDetail = itemView.findViewById(R.id.tv_departure_time_label);
        txtJourneyDetail = itemView.findViewById(R.id.tv_journey_detail_label);
        txtAirlineName = itemView.findViewById(R.id.airline_name);
        txtDuration = itemView.findViewById(R.id.duration);
        context = itemView.getContext();

    }

    @Override
    public void bind(FlightCancellationViewModel element) {

    }
}
