package com.tokopedia.ride.bookingride;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.presenter.BaseView;

/**
 * Created by alvarisi on 3/13/17.
 */

public interface BookingRideContract {
    interface View extends CustomerView{
        Context getActivityContext();

        boolean isUserLoggedIn();

        void navigateToLoginPage();

        void showVerificationPhoneNumberPage();

        boolean isUserPhoneNumberVerified();

        void prepareMainView();

        Activity getActivity();

        void showMessage(String message);

        void hideMessage(String message);

        void moveToCurrentLocation(double latitude, double longitude);

        void renderUberProductView();
    }

    interface Presenter extends CustomerPresenter<View>{
        void initialize();

    }
}
