package com.tokopedia.flight.cancellation.view.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    public interface FlightCancellationListener {
        void onPassengerChecked(FlightCancellationPassengerViewModel passengerViewModel, int position);
        void onPassengerUnchecked(FlightCancellationPassengerViewModel passengerViewModel, int position);
    }

    private Context context;
    private TextView txtDepartureDetail;
    private TextView txtJourneyDetail;
    private TextView txtAirlineName;
    private TextView txtDuration;
    private VerticalRecyclerView verticalRecyclerView;
    private PassengerAdapter passengerAdapter;
    private CheckBox checkBoxFlight;

    private FlightCancellationListener listener;

    public FlightCancellationViewHolder(View itemView, FlightCancellationListener flightCancellationListener) {
        super(itemView);

        context = itemView.getContext();
        listener = flightCancellationListener;

        txtDepartureDetail = itemView.findViewById(R.id.tv_departure_time_label);
        txtJourneyDetail = itemView.findViewById(R.id.tv_journey_detail_label);
        txtAirlineName = itemView.findViewById(R.id.airline_name);
        txtDuration = itemView.findViewById(R.id.duration);
        checkBoxFlight = itemView.findViewById(R.id.checkbox);

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

        checkBoxFlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    passengerAdapter.checkAllData();
                } else {
                    passengerAdapter.uncheckAllData();
                }
            }
        });
    }

    private class PassengerAdapter extends RecyclerView.Adapter<PassengerViewHolder> {

        List<FlightCancellationPassengerViewModel> passengerViewModelList;
        List<PassengerViewHolder> passengerViewHolderList = new ArrayList<>();

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
            passengerViewHolder.bindData(passengerViewModelList.get(position), getAdapterPosition());
            passengerViewHolderList.add(passengerViewHolder);
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

        public void checkAllData() {
            for (int index = 0; index < getItemCount(); index++) {
                passengerViewHolderList.get(index).updateCheckedButton(true);
            }
            notifyDataSetChanged();
        }

        public void uncheckAllData() {
            for (int index = 0; index < getItemCount(); index++) {
                passengerViewHolderList.get(index).updateCheckedButton(false);
            }
            notifyDataSetChanged();
        }
    }

    private class PassengerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtPassengerName;
        private TextView txtPassengerType;
        private CheckBox checkBoxPassenger;
        private boolean isPassengerChecked = false;
        private FlightCancellationPassengerViewModel passengerViewModel;
        private int adapterPosition = -1;

        public PassengerViewHolder(View itemView) {
            super(itemView);
            txtPassengerName = itemView.findViewById(R.id.tv_passenger_name);
            txtPassengerType = itemView.findViewById(R.id.tv_passenger_type);
            checkBoxPassenger = itemView.findViewById(R.id.checkbox);
            checkBoxPassenger.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isPassengerChecked = isChecked;

                    if (isChecked) {
                        listener.onPassengerChecked(passengerViewModel, adapterPosition);
                    } else {
                        listener.onPassengerUnchecked(passengerViewModel, adapterPosition);
                    }
                }
            });
        }

        public void bindData(FlightCancellationPassengerViewModel passengerViewModel, int adapterPosition) {
            this.passengerViewModel = passengerViewModel;
            this.adapterPosition = adapterPosition;

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

        public void updateCheckedButton(boolean checkedStatus) {
            checkBoxPassenger.setChecked(checkedStatus);
        }

        public boolean isPassengerChecked() {
            return isPassengerChecked;
        }

        public FlightCancellationPassengerViewModel getPassengerViewModel() {
            return passengerViewModel;
        }
    }
}
