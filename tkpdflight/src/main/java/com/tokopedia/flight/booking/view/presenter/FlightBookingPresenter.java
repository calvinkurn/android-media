package com.tokopedia.flight.booking.view.presenter;

import android.util.Patterns;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;

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
