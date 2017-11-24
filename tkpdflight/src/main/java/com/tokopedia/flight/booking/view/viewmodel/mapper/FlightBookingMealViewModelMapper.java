package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.AmenityItem;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingMealViewModelMapper {
    @Inject
    public FlightBookingMealViewModelMapper() {
    }

    public List<FlightBookingMealViewModel> transform(Amenity entity) {
        List<FlightBookingMealViewModel> viewModels = new ArrayList<>();
        FlightBookingMealViewModel data = null;
        if (entity != null) {
            for (AmenityItem item : entity.getItems()) {
                data = transform(entity.getKey(), item);
                if (data != null) {
                    viewModels.add(data);
                }
            }
        }
        return viewModels;
    }

    private FlightBookingMealViewModel transform(String key, AmenityItem item) {
        FlightBookingMealViewModel viewModel = null;
        if (item != null) {
            viewModel = new FlightBookingMealViewModel();
            viewModel.setId(item.getId());
            viewModel.setPrice(item.getPrice());
            viewModel.setTitle(item.getDescription());
        }
        return viewModel;
    }

}
