package com.tokopedia.flight.booking.view.viewmodel;

import com.tokopedia.flight.booking.data.cloud.entity.NewFarePrice;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingCartData {
    private String id;
    private int refreshTime;
    private FlightSearchViewModel departureTrip;
    private FlightSearchViewModel returnTrip;
    private List<FlightBookingLuggageMetaViewModel> luggageViewModels;
    private List<FlightBookingMealMetaViewModel> mealViewModels;
    private List<NewFarePrice> newFarePrices;

    public FlightBookingCartData() {
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public List<FlightBookingLuggageMetaViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    public void setLuggageViewModels(List<FlightBookingLuggageMetaViewModel> luggageViewModels) {
        this.luggageViewModels = luggageViewModels;
    }

    public List<FlightBookingMealMetaViewModel> getMealViewModels() {
        return mealViewModels;
    }

    public void setMealViewModels(List<FlightBookingMealMetaViewModel> mealViewModels) {
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
}
