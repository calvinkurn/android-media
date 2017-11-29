package com.tokopedia.flight.booking.view.viewmodel.mapper;

import com.tokopedia.flight.booking.data.cloud.entity.Amenity;
import com.tokopedia.flight.booking.data.cloud.entity.AmenityItem;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/15/17.
 */

public class FlightBookingLuggageViewModelMapper {
    @Inject
    public FlightBookingLuggageViewModelMapper() {
    }

    public List<FlightBookingLuggageViewModel> transform(Amenity entity) {
        List<FlightBookingLuggageViewModel> viewModels = new ArrayList<>();
        FlightBookingLuggageViewModel data = null;
        if (entity != null) {
            for (AmenityItem item : entity.getItems()) {
                data = transform(item);
                if (data != null) {
                    viewModels.add(data);
                }
            }
        }
        return viewModels;
    }

    private FlightBookingLuggageViewModel transform(AmenityItem item) {
        FlightBookingLuggageViewModel viewModel = null;
        if (item != null) {
            viewModel = new FlightBookingLuggageViewModel();
            viewModel.setId(item.getId());
            viewModel.setPriceNumeric(item.getPriceNumeric());
            viewModel.setPrice(item.getPrice());
            viewModel.setTitle(item.getDescription());
        }
        return viewModel;
    }

}
