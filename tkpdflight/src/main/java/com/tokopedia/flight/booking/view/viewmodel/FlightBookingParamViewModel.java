package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * Created by alvarisi on 11/9/17.
 */

public class FlightBookingParamViewModel {
    private FlightSearchViewModel departureTrip;
    private FlightSearchViewModel returnTrip;
    private FlightSearchPassDataViewModel searchParam;
    private FlightBookingPhoneCodeViewModel phoneCodeViewModel;
    private List<FlightBookingPassengerViewModel> passengerViewModels;

    public FlightBookingParamViewModel() {
    }

    public FlightBookingPhoneCodeViewModel getPhoneCodeViewModel() {
        return phoneCodeViewModel;
    }

    public void setPhoneCodeViewModel(FlightBookingPhoneCodeViewModel phoneCodeViewModel) {
        this.phoneCodeViewModel = phoneCodeViewModel;
    }

    public List<FlightBookingPassengerViewModel> getPassengerViewModels() {
        return passengerViewModels;
    }

    public void setPassengerViewModels(List<FlightBookingPassengerViewModel> passengerViewModels) {
        this.passengerViewModels = passengerViewModels;
    }

    public FlightSearchViewModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(FlightSearchViewModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public FlightSearchViewModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(FlightSearchViewModel returnTrip) {
        this.returnTrip = returnTrip;
    }

    public FlightSearchPassDataViewModel getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(FlightSearchPassDataViewModel searchParam) {
        this.searchParam = searchParam;
    }
}
