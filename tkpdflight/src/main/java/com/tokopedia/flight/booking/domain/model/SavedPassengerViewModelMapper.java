package com.tokopedia.flight.booking.domain.model;

import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 23/02/18.
 */

public class SavedPassengerViewModelMapper {

    @Inject
    public SavedPassengerViewModelMapper() {
    }

    public FlightBookingPassengerViewModel transform(SavedPassengerEntity savedPassengerEntity) {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = new FlightBookingPassengerViewModel();
        if (savedPassengerEntity.getPassengerAttribute().getDob() != null) {
            flightBookingPassengerViewModel.setPassengerBirthdate(
                    FlightDateUtil.formatDate(
                            FlightDateUtil.FORMAT_DATE_API,
                            FlightDateUtil.DEFAULT_FORMAT,
                            savedPassengerEntity.getPassengerAttribute().getDob()
                    )
            );
        }
        flightBookingPassengerViewModel.setPassengerFirstName(savedPassengerEntity.getPassengerAttribute().getFirstName());
        flightBookingPassengerViewModel.setPassengerLastName(savedPassengerEntity.getPassengerAttribute().getLastName());
        flightBookingPassengerViewModel.setPassengerTitleId(savedPassengerEntity.getPassengerAttribute().getTitle());
        flightBookingPassengerViewModel.setPassengerId(savedPassengerEntity.getId());

        return flightBookingPassengerViewModel;
    }

    public List<FlightBookingPassengerViewModel> transform(List<SavedPassengerEntity> savedPassengerEntityList) {
        List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList = new ArrayList<>();

        for (SavedPassengerEntity savedPassengerEntity : savedPassengerEntityList) {
            flightBookingPassengerViewModelList.add(this.transform(savedPassengerEntity));
        }

        return flightBookingPassengerViewModelList;
    }
}
