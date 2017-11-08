package com.tokopedia.flight.search.data.cloud.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by User on 11/8/2017.
 */

public class FlightSearchSingleRequestData {
    public static final String SEARCH_SINGLE = "search_single";
    @SerializedName("type")
    private String type;
    @SerializedName("attributes")
    private Attributes attributes;

    public FlightSearchSingleRequestData(FlightSearchPassDataViewModel flightSearchPassDataViewModel,
                                         boolean isReturning) {
        type = SEARCH_SINGLE;
        FlightPassengerViewModel flightPassengerViewModel = flightSearchPassDataViewModel.getFlightPassengerViewModel();
        attributes = new Attributes(Integer.parseInt(flightSearchPassDataViewModel.getFlightClass().getId()), flightPassengerViewModel.getAdult(),
                flightPassengerViewModel.getChildren(), flightPassengerViewModel.getInfant());
        if (isReturning) {
            attributes.setDeparture(flightSearchPassDataViewModel.getArrivalAirport().getAirportId());
            attributes.setArrival(flightSearchPassDataViewModel.getDepartureAirport().getAirportId());
            attributes.setDate(flightSearchPassDataViewModel.getReturnDate());
        } else {
            attributes.setDeparture(flightSearchPassDataViewModel.getDepartureAirport().getAirportId());
            attributes.setArrival(flightSearchPassDataViewModel.getArrivalAirport().getAirportId());
            attributes.setDate(flightSearchPassDataViewModel.getDepartureDate());
        }
    }
}
