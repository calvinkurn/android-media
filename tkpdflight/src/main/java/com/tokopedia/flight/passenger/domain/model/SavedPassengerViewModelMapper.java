package com.tokopedia.flight.passenger.domain.model;

import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
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

    public FlightBookingPassengerViewModel transform(FlightPassengerDb savedPassengerEntity) {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = new FlightBookingPassengerViewModel();
        if (savedPassengerEntity.getBirthdate() != null) {
            flightBookingPassengerViewModel.setPassengerBirthdate(
                    FlightDateUtil.formatDate(
                            FlightDateUtil.FORMAT_DATE_API,
                            FlightDateUtil.DEFAULT_FORMAT,
                            savedPassengerEntity.getBirthdate()
                    )
            );
        }

        flightBookingPassengerViewModel.setPassengerFirstName(savedPassengerEntity.getFirstName());
        flightBookingPassengerViewModel.setPassengerLastName(savedPassengerEntity.getLastName());
        flightBookingPassengerViewModel.setPassengerTitleId(savedPassengerEntity.getTitleId());
        flightBookingPassengerViewModel.setPassengerId(savedPassengerEntity.getPassengerId());

        return flightBookingPassengerViewModel;
    }

    public List<FlightBookingPassengerViewModel> transform(List<FlightPassengerDb> savedPassengerEntityList) {
        List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList = new ArrayList<>();

        for (FlightPassengerDb savedPassengerEntity : savedPassengerEntityList) {
            flightBookingPassengerViewModelList.add(this.transform(savedPassengerEntity));
        }

        return flightBookingPassengerViewModelList;
    }
}

