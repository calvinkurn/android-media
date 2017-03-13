package com.tokopedia.ride.bookingride;

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

        void showVerificationPhoneNumberDialog();

        boolean isUserPhoneNumberVerified();

        void prepareMainView();
    }

    interface Presenter extends CustomerPresenter<View>{
        void initialize();

    }
}
