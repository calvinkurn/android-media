package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import java.util.List;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingCartData {
    private String id;
    private int refreshTime;
    private FlightBookingPhoneCodeViewModel defaultPhoneCode;
    private FlightDetailViewModel departureTrip;
    private FlightDetailViewModel returnTrip;
    private List<FlightBookingAmenityMetaViewModel> luggageViewModels;
    private List<FlightBookingAmenityMetaViewModel> mealViewModels;
    private List<NewFarePrice> newFarePrices;

    public FlightBookingCartData() {
    }

    public int getRefreshTime() {
        return refreshTime;
//        return 15;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<FlightBookingAmenityMetaViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    public void setLuggageViewModels(List<FlightBookingAmenityMetaViewModel> luggageViewModels) {
        this.luggageViewModels = luggageViewModels;
    }

    public List<FlightBookingAmenityMetaViewModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingAmenityMetaViewModel> mealViewModels) {
        this.mealViewModels = mealViewModels;
    }

    public List<NewFarePrice> getNewFarePrices() {
        return newFarePrices;
    }

    public void setNewFarePrices(List<NewFarePrice> newFarePrices) {
        this.newFarePrices = newFarePrices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FlightDetailViewModel getDepartureTrip() {
        return departureTrip;
    }

    public void setDepartureTrip(FlightDetailViewModel departureTrip) {
        this.departureTrip = departureTrip;
    }

    public FlightDetailViewModel getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(FlightDetailViewModel returnTrip) {
        this.returnTrip = returnTrip;
    }

    public FlightBookingPhoneCodeViewModel getDefaultPhoneCode() {
        return defaultPhoneCode;
    }

    public void setDefaultPhoneCode(FlightBookingPhoneCodeViewModel defaultPhoneCode) {
        this.defaultPhoneCode = defaultPhoneCode;
    }
}
