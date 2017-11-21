package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.List;

/**
 * @author by alvarisi on 11/9/17.
 */

public class FlightBookingParamViewModel {
    private String id;
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

    public FlightSearchPassDataViewModel getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(FlightSearchPassDataViewModel searchParam) {
        this.searchParam = searchParam;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
