package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.search.data.cloud.model.response.Route;

import java.util.List;

/**
 * Created by alvarisi on 11/7/17.
 */

public class FlightBookingLuggageRouteViewModel {
    private Route route;
    private List<FlightBookingLuggageViewModel> luggage;

    public FlightBookingLuggageRouteViewModel() {
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<FlightBookingLuggageViewModel> getLuggage() {
        return luggage;
    }

    public void setLuggage(List<FlightBookingLuggageViewModel> luggage) {
        this.luggage = luggage;
    }

}
