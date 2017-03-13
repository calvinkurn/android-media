package com.tokopedia.ride.bookingride;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by alvarisi on 3/13/17.
 */

public class BookingRidePresenter extends BaseDaggerPresenter<BookingRideContract.View>
        implements BookingRideContract.Presenter {
    public BookingRidePresenter() {
    }

    @Override
    public void initialize() {
        if (getView().isUserLoggedIn()) {
            if (getView().isUserPhoneNumberVerified()) {
                getView().showVerificationPhoneNumberDialog();
            } else {
                getView().prepareMainView();
            }
        } else {
            getView().navigateToLoginPage();
        }
    }
}
