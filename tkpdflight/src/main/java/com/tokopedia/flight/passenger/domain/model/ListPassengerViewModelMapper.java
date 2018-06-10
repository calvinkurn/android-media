package com.tokopedia.flight.passenger.domain.model;

import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 23/02/18.
 */

public class ListPassengerViewModelMapper {

    @Inject
    public ListPassengerViewModelMapper() {
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

        if (savedPassengerEntity.getPassportNo() != null) {
            flightBookingPassengerViewModel.setPassportNumber(savedPassengerEntity.getPassportNo());
        }

        if (savedPassengerEntity.getPassportExpiry() != null) {
            flightBookingPassengerViewModel.setPassportExpiredDate(savedPassengerEntity.getPassportExpiry());
        }

        if (savedPassengerEntity.getPassportNationality() != null) {
            FlightBookingPhoneCodeViewModel passportNationality = new FlightBookingPhoneCodeViewModel();
            passportNationality.setCountryId(savedPassengerEntity.getPassportNationality());
            flightBookingPassengerViewModel.setPassportNationality(passportNationality);
        }

        if (savedPassengerEntity.getPassportCountry() != null) {
            FlightBookingPhoneCodeViewModel passportIssuerCountry = new FlightBookingPhoneCodeViewModel();
            passportIssuerCountry.setCountryId(savedPassengerEntity.getPassportCountry());
            flightBookingPassengerViewModel.setPassportIssuerCountry(passportIssuerCountry);
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

