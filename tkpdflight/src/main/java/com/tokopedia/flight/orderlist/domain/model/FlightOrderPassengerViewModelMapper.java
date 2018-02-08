package com.tokopedia.flight.orderlist.domain.model;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerAmentityEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 12/12/17.
 */

public class FlightOrderPassengerViewModelMapper {
    @Inject
    public FlightOrderPassengerViewModelMapper() {
    }

    public FlightOrderPassengerViewModel transform(PassengerEntity passengerEntity) {
        FlightOrderPassengerViewModel viewModel = null;
        if (passengerEntity != null) {
            viewModel = new FlightOrderPassengerViewModel();
            viewModel.setPassengerFirstName(passengerEntity.getFirstName());
            viewModel.setPassengerLastName(passengerEntity.getLastName());
            viewModel.setPassengerBirthdate(passengerEntity.getDob());
            viewModel.setPassengerTitleId(passengerEntity.getTitle());
            viewModel.setType(passengerEntity.getType());
            viewModel.setAmenities(transformAmenities(passengerEntity.getAmenities()));
        }
        return viewModel;
    }

    private List<FlightBookingAmenityViewModel> transformAmenities(List<PassengerAmentityEntity> amenities) {
        List<FlightBookingAmenityViewModel> viewModels = new ArrayList<>();

        for (PassengerAmentityEntity amentityEntity : amenities) {
            FlightBookingAmenityViewModel amenityViewModel = new FlightBookingAmenityViewModel();
            amenityViewModel.setId(String.valueOf(amentityEntity.getSequence()));
            amenityViewModel.setPrice(amentityEntity.getPrice());
            amenityViewModel.setPriceNumeric(amentityEntity.getPriceNumeric());
            amenityViewModel.setTitle(amentityEntity.getDetail());
            amenityViewModel.setDepartureId(amentityEntity.getDepartureAirportId());
            amenityViewModel.setArrivalId(amentityEntity.getArrivalAirportId());
            amenityViewModel.setAmenityType(amentityEntity.getAmenityType());
            viewModels.add(amenityViewModel);
        }
        return viewModels;
    }

    public List<FlightOrderPassengerViewModel> transform(List<PassengerEntity> entities) {
        List<FlightOrderPassengerViewModel> viewModels = new ArrayList<>();
        FlightOrderPassengerViewModel viewModel;
        for (PassengerEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }
}
