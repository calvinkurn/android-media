package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListViewHolder extends AbstractViewHolder<FlightCancellationListViewModel> {

    public static final int LAYOUT = R.layout.item_flight_cancellation_list;

    TextViewCompat txtCancellationId;
    TextViewCompat txtJourney;
    TextViewCompat txtCreatedTime;
    Context context;

    public FlightCancellationListViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        txtCancellationId = itemView.findViewById(R.id.txt_cancellation_id);
        txtJourney = itemView.findViewById(R.id.txt_cancellation_journey);
        txtCreatedTime = itemView.findViewById(R.id.txt_cancellation_created_time);
    }

    @Override
    public void bind(FlightCancellationListViewModel element) {
        txtCancellationId.setText(String.format(getString(R.string.flight_cancellation_list_id),
                element.getCancellations().getRefundId()));
        txtCreatedTime.setText(String.format(getString(R.string.flight_cancellation_list_created_time),
                element.getCancellations().getCreateTime()));
        txtJourney.setText(String.format(getString(R.string.flight_label_detail_format),
                element.getCancellations().getJourneys().get(0).getDepartureCity(),
                element.getCancellations().getJourneys().get(0).getDepartureAiportId(),
                element.getCancellations().getJourneys().get(0).getArrivalCity(),
                element.getCancellations().getJourneys().get(0).getArrivalAirportId()));
    }

}
