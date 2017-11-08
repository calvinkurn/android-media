package com.tokopedia.flight.booking.view.presenter;

import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by alvarisi on 11/8/17.
 */

public class FlightBookingPresenter extends BaseDaggerPresenter<FlightBookingContract.View> implements FlightBookingContract.Presenter {

    @Inject
    public FlightBookingPresenter() {

    }

    @Override
    public void onButtonSubmitClicked() {
        if (validateFields()) {

        }
    }

    @Override
    public void initialize() {
        if (isRoundTrip()) {
            getView().showAndRenderDepartureTripCardDetail(getView().getCurrentTripViewModel().getSearchParam(),getView().getCurrentTripViewModel().getDepartureTrip());
            getView().showAndRenderReturnTripCardDetail(getView().getCurrentTripViewModel().getSearchParam(), getView().getCurrentTripViewModel().getReturnTrip());
        } else {
            getView().showAndRenderDepartureTripCardDetail(getView().getCurrentTripViewModel().getSearchParam(), getView().getCurrentTripViewModel().getDepartureTrip());
        }

        List<FlightBookingPassengerViewModel> passengerViewModels = buildPassengerViewModel(getView().getCurrentTripViewModel().getSearchParam());
        getView().renderPassengersList(passengerViewModels);
        // TODO : Calculate and render "Rincian Harga"
    }

    private List<FlightBookingPassengerViewModel> buildPassengerViewModel(FlightSearchPassDataViewModel passData) {
        boolean isSingleRoute = !isRoundTrip();
        int passengerNumber = 1;
        List<FlightBookingPassengerViewModel> viewModels = new ArrayList<>();
        for (int i = 1, adultTotal = passData.getFlightPassengerViewModel().getAdult(); i <= adultTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setType(FlightBookingPassenger.ADULT);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_adult_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        for (int i = 1, childTotal = passData.getFlightPassengerViewModel().getChildren(); i <= childTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setType(FlightBookingPassenger.CHILDREN);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_children_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        for (int i = 1, infantTotal = passData.getFlightPassengerViewModel().getChildren(); i <= infantTotal; i++) {
            FlightBookingPassengerViewModel viewModel = new FlightBookingPassengerViewModel();
            viewModel.setType(FlightBookingPassenger.INFANT);
            viewModel.setSingleRoute(isSingleRoute);
            viewModel.setHeaderTitle(String.format("%s %d (%s)",
                    getView().getString(R.string.flight_booking_prefix_passenger),
                    passengerNumber,
                    getView().getString(R.string.flight_booking_postfix_infant_passenger))
            );
            viewModels.add(viewModel);
            passengerNumber++;
        }
        return viewModels;
    }

    private boolean isRoundTrip() {
        return getView().getCurrentTripViewModel().getReturnTrip() != null;
    }

    private boolean validateFields() {
        boolean isValid = true;
        if (getView().getContactName().length() == 0) {
            isValid = false;
            getView().showContactNameEmptyError(R.string.flight_booking_contact_name_empty_error);
        } else if (getView().getContactEmail().length() == 0) {
            isValid = false;
            getView().showContactEmailEmptyError(R.string.flight_booking_contact_email_empty_error);
        } else if (isValidEmail(getView().getContactEmail())) {
            isValid = false;
            getView().showContactEmailInvalidError(R.string.flight_booking_contact_email_invalid_error);
        } else if (getView().getContactPhoneNumber().length() == 0) {
            isValid = false;
            getView().showContactPhoneNumberEmptyError(R.string.flight_booking_contact_phone_empty_error);
        }
        return isValid;
    }

    private boolean isValidEmail(String contactEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches();
    }
}
