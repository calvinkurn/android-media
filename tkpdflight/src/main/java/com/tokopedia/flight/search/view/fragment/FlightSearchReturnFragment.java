package com.tokopedia.flight.search.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.activity.FlightSearchReturnActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by hendry on 10/26/2017.
 */

public class FlightSearchReturnFragment extends FlightSearchFragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_return, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        airlineName = (TextView) view.findViewById(R.id.airline_name);
        duration = (TextView) view.findViewById(R.id.duration);

        String departureAirportId = TextUtils.isEmpty(flightSearchPassDataViewModel.getArrivalAirport().getAirportId()) ?
                flightSearchPassDataViewModel.getArrivalAirport().getCityId() : flightSearchPassDataViewModel.getArrivalAirport().getAirportId();
        departureAirportCode.setText(departureAirportId);
        departureAirportName.setText(flightSearchPassDataViewModel.getArrivalAirport().getCityName());
        String arrivalAirportId = TextUtils.isEmpty(flightSearchPassDataViewModel.getDepartureAirport().getAirportId()) ?
                flightSearchPassDataViewModel.getDepartureAirport().getCityId() : flightSearchPassDataViewModel.getDepartureAirport().getAirportId();
        arrivalAirportCode.setText(arrivalAirportId);
        arrivalAirportName.setText(flightSearchPassDataViewModel.getDepartureAirport().getCityName());
    }

    @Override
    public void loadData(int page, int currentDataSize, int rowPerPage) {
        super.loadData(page, currentDataSize, rowPerPage);
        flightSearchPresenter.getDetailDepartureFlight(selectedFlightDeparture);
    }

    protected boolean isReturning(){
        return true;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onSuccessGetDetailFlightDeparture(FlightSearchViewModel flightSearchViewModel) {
        if(flightSearchViewModel.getAirlineList().size() > 1){
            airlineName.setText(getString(R.string.flight_label_multi_maskapai));
        }else if(flightSearchViewModel.getAirlineList().size() == 1){
            airlineName.setText(flightSearchViewModel.getAirlineList().get(0).getName());
        }
        if(flightSearchViewModel.getAddDayArrival() > 0) {
            duration.setText(String.format("| %s - %s (+%sh)", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime(), String.valueOf(flightSearchViewModel.getAddDayArrival())));
        }else{
            duration.setText(String.format("| %s - %s", flightSearchViewModel.getDepartureTime(),
                    flightSearchViewModel.getArrivalTime()));
        }
    }
}
