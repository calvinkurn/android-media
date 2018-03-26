package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

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

        String departureCityAirportCode = (element.getFlightCancellationJourney().getDepartureCityCode().isEmpty() ||
                element.getFlightCancellationJourney().getDepartureCityCode().length() == 0) ?
                element.getFlightCancellationJourney().getDepartureAiportId() :
                element.getFlightCancellationJourney().getDepartureCityCode();
        String arrivalCityAirportCode = (element.getFlightCancellationJourney().getArrivalCityCode().isEmpty() ||
                element.getFlightCancellationJourney().getArrivalCityCode().length() == 0) ?
                element.getFlightCancellationJourney().getArrivalAirportId() :
                element.getFlightCancellationJourney().getArrivalCityCode();
        String departureDate = FlightDateUtil.formatDate(
                FlightDateUtil.FORMAT_DATE_API,
                FlightDateUtil.FORMAT_DATE,
                element.getFlightCancellationJourney().getDepartureTime());
        String departureTime = FlightDateUtil.formatDate(
                FlightDateUtil.FORMAT_DATE_API,
                FlightDateUtil.FORMAT_TIME_DETAIL,
                element.getFlightCancellationJourney().getDepartureTime());
        String arrivalTime = FlightDateUtil.formatDate(
                FlightDateUtil.FORMAT_DATE_API,
                FlightDateUtil.FORMAT_TIME_DETAIL,
                element.getFlightCancellationJourney().getArrivalTime());


        txtDepartureDetail.setText(
                String.format("Penerbangan %d - %s",
                        getAdapterPosition() + 1,
                        departureDate)
        );
        txtJourneyDetail.setText(
                String.format("%s (%s) - %s (%s)",
                        element.getFlightCancellationJourney().getDepartureCity(),
                        departureCityAirportCode,
                        element.getFlightCancellationJourney().getArrivalCity(),
                        arrivalCityAirportCode)
        );
        txtAirlineName.setText(element.getFlightCancellationJourney().getAirlineName());
        txtDuration.setText(
                String.format(getString(R.string.flight_booking_trip_info_airport_format),
                        departureTime,
                        arrivalTime)
        );
    }
}
