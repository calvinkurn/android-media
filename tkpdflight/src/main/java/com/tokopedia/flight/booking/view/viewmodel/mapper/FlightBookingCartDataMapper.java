package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;

import javax.inject.Inject;

/**
 * Created by alvarisi on 11/15/17.
 */

public class FlightBookingCartDataMapper {
    private FlightBookingMealViewModelMapper flightBookingMealViewModelMapper;
    private FlightBookingLuggageViewModelMapper flightBookingLuggageViewModelMapper;

    @Inject
    public FlightBookingCartDataMapper(FlightBookingMealViewModelMapper flightBookingMealViewModelMapper,
                                       FlightBookingLuggageViewModelMapper flightBookingLuggageViewModelMapper) {
        this.flightBookingMealViewModelMapper = flightBookingMealViewModelMapper;
        this.flightBookingLuggageViewModelMapper = flightBookingLuggageViewModelMapper;
    }

    public FlightBookingCartData transform(CartEntity entity) {
        FlightBookingCartData data = null;
        if (entity != null) {
            data = new FlightBookingCartData();
            data.setId(entity.getId());
            data.setRefreshTime(entity.getAttribute().getFlightAttribute().getRefreshTime());
            if (entity.getAttribute().getFlightAttribute().getAmenities() != null) {
                for (Amenity amenity : entity.getAttribute().getFlightAttribute().getAmenities()) {
                    switch (amenity.getType()) {
                        case Amenity.MEAL:
                            data.setMealViewModels(flightBookingMealViewModelMapper.transform(amenity));
                            break;
                        case Amenity.LUGGAGE:
                            data.setLuggageViewModels(flightBookingLuggageViewModelMapper.transform(amenity));
                            break;
                    }
                }
            }
            data.setNewFarePrices(entity.getAttribute().getFlightAttribute().getNewPrices());
        }
        return data;
    }
}
