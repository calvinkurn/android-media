package com.tokopedia.flight.orderlist.domain.model.mapper;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.cancellation.constant.FlightCancellationStatus;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationDetailsAttribute;
import com.tokopedia.flight.orderlist.data.cloud.entity.CancellationEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerAmentityEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.PassengerEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderPassengerViewModel;

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

    public List<FlightBookingAmenityViewModel> transformAmenities(List<PassengerAmentityEntity> amenities) {
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

    public List<FlightOrderPassengerViewModel> transform(List<PassengerEntity> entities, List<CancellationEntity> cancellations) {
        List<FlightOrderPassengerViewModel> viewModels = new ArrayList<>();
        FlightOrderPassengerViewModel viewModel;
        for (PassengerEntity entity : entities) {
            viewModel = transform(entity);
            if (viewModel != null) {
                viewModel.setStatus(getPassengerStatus(entity.getId(), cancellations));
                viewModels.add(viewModel);
            }
        }
        return viewModels;
    }

    private int getPassengerStatus(String passengerId, List<CancellationEntity> cancellations) {
        int status = 0;
        CancellationDetailsAttribute passenger = new CancellationDetailsAttribute();
        passenger.setPassengerId(passengerId);

        for (CancellationEntity item : cancellations) {
            if (item.getDetails().contains(passenger)) {
                if (item.getStatus() == FlightCancellationStatus.PENDING ||
                        item.getStatus() == FlightCancellationStatus.REFUNDED ||
                        item.getStatus() == FlightCancellationStatus.REQUESTED) {
                    status = item.getStatus();
                    break;
                } else if (item.getStatus() == FlightCancellationStatus.ABORTED) {
                    status = item.getStatus();
                }
            }
        }
        return status;
    }
}
