package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;

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
    private VerticalRecyclerView verticalRecyclerView;
    private PassengerAdapter passengerAdapter;

    public FlightCancellationViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        txtDepartureDetail = itemView.findViewById(R.id.tv_departure_time_label);
        txtJourneyDetail = itemView.findViewById(R.id.tv_journey_detail_label);
        txtAirlineName = itemView.findViewById(R.id.airline_name);
        txtDuration = itemView.findViewById(R.id.duration);

        verticalRecyclerView = itemView.findViewById(R.id.recycler_view_passenger);
        verticalRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        passengerAdapter = new PassengerAdapter();
        verticalRecyclerView.setAdapter(passengerAdapter);

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

        passengerAdapter.addData(element.getPassengerViewModelList());
    }

    private class PassengerAdapter extends RecyclerView.Adapter<PassengerViewHolder> {

        List<FlightCancellationPassengerViewModel> passengerViewModelList;

        public PassengerAdapter() {
            this.passengerViewModelList = new ArrayList<>();
        }

        @Override
        public PassengerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_flight_cancellation_passenger, viewGroup, false);
            return new PassengerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PassengerViewHolder passengerViewHolder, int position) {
            passengerViewHolder.bindData(passengerViewModelList.get(position));
        }

        @Override
        public int getItemCount() {
            return passengerViewModelList.size();
        }

        public void addData(List<FlightCancellationPassengerViewModel> passengerViewModelList) {
            this.passengerViewModelList.clear();
            this.passengerViewModelList.addAll(passengerViewModelList);
            notifyDataSetChanged();
        }
    }

    private class PassengerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPassengerName;
        private TextView txtPassengerType;

        public PassengerViewHolder(View itemView) {
            super(itemView);
            txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
            txtPassengerType = itemView.findViewById(R.id.tv_passenger_type);
        }

        public void bindData(FlightCancellationPassengerViewModel passengerViewModel) {
            txtPassengerName.setText(String.format("%s %s %s", passengerViewModel.getTitleString(),
                    passengerViewModel.getFirstName(), passengerViewModel.getLastName()));

            switch (passengerViewModel.getType()) {
                case ADULT:
                    txtPassengerType.setText(R.string.flightbooking_price_adult_label);
                    break;
                case CHILDREN:
                    txtPassengerType.setText(R.string.flightbooking_price_child_label);
                    break;
                case INFANT:
                    txtPassengerType.setText(R.string.flightbooking_price_infant_label);
                    break;
                default:
                    txtPassengerType.setText(R.string.flightbooking_price_adult_label);
            }
        }

    }
}
