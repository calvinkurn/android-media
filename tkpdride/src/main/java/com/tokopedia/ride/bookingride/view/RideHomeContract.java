package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 4/21/17.
 */

public interface RideHomeContract {
    interface View extends CustomerView {
        boolean isUserLoggedIn();

        void navigateToLoginPage();

        void showVerificationPhoneNumberPage();

        boolean isUserPhoneNumberVerified();

        void inflateMapAndProductFragment();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
