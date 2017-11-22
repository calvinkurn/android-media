package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.CartEntity;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingCartData;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;

import java.util.ArrayList;
import java.util.List;

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
                List<FlightBookingLuggageMetaViewModel> luggageMetaViewModels = new ArrayList<>();
                List<FlightBookingMealMetaViewModel> mealMetaViewModels = new ArrayList<>();
                for (Amenity amenity : entity.getAttribute().getFlightAttribute().getAmenities()) {
                    switch (amenity.getType()) {
                        case Amenity.MEAL:
                            FlightBookingMealMetaViewModel mealMetaViewModel = new FlightBookingMealMetaViewModel();
                            mealMetaViewModel.setKey(amenity.getKey());
                            mealMetaViewModel.setDescription(amenity.getDescription());
                            mealMetaViewModel.setMealViewModels(flightBookingMealViewModelMapper.transform(amenity));
                            mealMetaViewModels.add(mealMetaViewModel);
                            break;
                        case Amenity.LUGGAGE:
                            FlightBookingLuggageMetaViewModel luggageMetaViewModel = new FlightBookingLuggageMetaViewModel();
                            luggageMetaViewModel.setKey(amenity.getKey());
                            luggageMetaViewModel.setDescription(amenity.getDescription());
                            luggageMetaViewModel.setLuggages(flightBookingLuggageViewModelMapper.transform(amenity));
                            luggageMetaViewModels.add(luggageMetaViewModel);
                            break;
                    }
                }
                data.setLuggageViewModels(luggageMetaViewModels);
                data.setMealViewModels(mealMetaViewModels);
            } else {
                data.setLuggageViewModels(new ArrayList<FlightBookingLuggageMetaViewModel>());
                data.setMealViewModels(new ArrayList<FlightBookingMealMetaViewModel>());
            }
            data.setNewFarePrices(entity.getAttribute().getFlightAttribute().getNewPrices());
        }
        return data;
    }
}
