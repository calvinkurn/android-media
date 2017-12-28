package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.view.activity.FlightSearchReturnActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment {

    private AppCompatTextView departureHeaderLabel;
    private TextView airlineName;
    private TextView duration;

    private String selectedFlightDeparture;

    public static FlightSearchReturnFragment newInstance(FlightSearchPassDataViewModel passDataViewModel, String selectedDepartureID) {
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PASS_DATA, passDataViewModel);
        args.putString(FlightSearchReturnActivity.EXTRA_SEL_DEPARTURE_ID, selectedDepartureID);
        FlightSearchReturnFragment fragment = new FlightSearchReturnFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedFlightDeparture = getArguments().getString(FlightSearchReturnActivity.EXTRA_SEL_DEPARTURE_ID);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_search_return;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        airlineName = (TextView) view.findViewById(R.id.airline_name);
        duration = (TextView) view.findViewById(R.id.duration);
        departureHeaderLabel = (AppCompatTextView) view.findViewById(R.id.tv_departure_header_card_label);

        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture);
    }

    protected FlightAirportDB getDepartureAirport() {
        return flightSearchPassDataViewModel.getArrivalAirport();
    }

    protected FlightAirportDB getArrivalAirport() {
        return flightSearchPassDataViewModel.getDepartureAirport();
    }

    protected boolean isReturning() {
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel) {
        if (flightSearchViewModel.getAirlineList().size() > 1) {
            airlineName.setText(getString(R.string.flight_label_multi_maskapai));
        } else if (flightSearchViewModel.getAirlineList().size() == 1) {
            airlineName.setText(flightSearchViewModel.getAirlineList().get(0).getName());
        }
        if (flightSearchViewModel.getAddDayArrival() > 0) {
            duration.setText(String.format("| %s - %s (+%sh)", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime(), String.valueOf(flightSearchViewModel.getAddDayArrival())));
        } else {
            duration.setText(String.format("| %s - %s", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime()));
        }

        departureHeaderLabel.setText(String.format("%s - %s", getString(R.string.flight_label_departure_flight), FlightDateUtil.formatToUi(flightSearchPassDataViewModel.getDepartureDate())));
    }
}
