package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 11/15/17.
 */

public class FlightBookingCartDataMapper {
    private FlightBookingAmenityViewModelMapper flightBookingAmenityViewModelMapper;

    @Inject
    public FlightBookingCartDataMapper(FlightBookingAmenityViewModelMapper flightBookingAmenityViewModelMapper) {
        this.flightBookingAmenityViewModelMapper = flightBookingAmenityViewModelMapper;
    }

    public FlightBookingCartData transform(FlightBookingCartData data, CartEntity entity){
        if (entity != null) {
            if (data == null) {
                data = new FlightBookingCartData();
            }
            data.setId(entity.getId());
            data.setRefreshTime(entity.getAttribute().getFlightAttribute().getRefreshTime());
            if (entity.getAttribute().getFlightAttribute().getAmenities() != null) {
                List<FlightBookingAmenityMetaViewModel> luggageMetaViewModels = new ArrayList<>();
                List<FlightBookingAmenityMetaViewModel> mealMetaViewModels = new ArrayList<>();
                for (Amenity amenity : entity.getAttribute().getFlightAttribute().getAmenities()) {
                    switch (amenity.getType()) {
                        case Amenity.MEAL:
                            FlightBookingAmenityMetaViewModel mealMetaViewModel = new FlightBookingAmenityMetaViewModel();
                            mealMetaViewModel.setArrivalId(amenity.getArrivalId());
                            mealMetaViewModel.setDepartureId(amenity.getDepartureId());
                            mealMetaViewModel.setKey(amenity.getKey());
                            mealMetaViewModel.setJourneyId(amenity.getJourneyId());
                            mealMetaViewModel.setDescription(amenity.getDescription());
                            mealMetaViewModel.setAmenities(flightBookingAmenityViewModelMapper.transform(amenity));
                            mealMetaViewModels.add(mealMetaViewModel);
                            break;
                        case Amenity.LUGGAGE:
                            FlightBookingAmenityMetaViewModel luggageMetaViewModel = new FlightBookingAmenityMetaViewModel();
                            luggageMetaViewModel.setArrivalId(amenity.getArrivalId());
                            luggageMetaViewModel.setDepartureId(amenity.getDepartureId());
                            luggageMetaViewModel.setKey(amenity.getKey());
                            luggageMetaViewModel.setJourneyId(amenity.getJourneyId());
                            luggageMetaViewModel.setDescription(amenity.getDescription());
                            luggageMetaViewModel.setAmenities(flightBookingAmenityViewModelMapper.transform(amenity));
                            luggageMetaViewModels.add(luggageMetaViewModel);
                            break;
                    }
                }
                data.setLuggageViewModels(luggageMetaViewModels);
                data.setMealViewModels(mealMetaViewModels);
            } else {
                data.setLuggageViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
                data.setMealViewModels(new ArrayList<FlightBookingAmenityMetaViewModel>());
            }
            data.setNewFarePrices(entity.getAttribute().getFlightAttribute().getNewPrices());
        }
        return data;
    }

    public FlightBookingCartData transform(CartEntity entity) {
        FlightBookingCartData data = null;
        return transform(null, entity);
    }
}
