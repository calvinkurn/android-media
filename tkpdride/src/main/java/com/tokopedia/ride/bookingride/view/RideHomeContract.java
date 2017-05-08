package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

/**
 * Created by alvarisi on 4/21/17.
 */

public interface RideHomeContract {
    interface View extends CustomerView {
        boolean isUserLoggedIn();

        void navigateToLoginPage();

        void showVerificationPhoneNumberPage();

        boolean isUserPhoneNumberVerified();

        void showCheckPendingRequestLoading();

        RequestParams getCurrentRideRequestParam();

        void hideCheckPendingRequestLoading();

        void showRetryCheckPendingRequestLayout();

        void actionNavigateToOnTripScreen(RideRequest rideRequest);

        void inflateMapAndProductFragment();

        boolean isHavePendingRequestAndOpenedFromPushNotif();

        void closeScreen();

        void showRetryCheckPendingRequestLayout(String message);

        void actionInflateInitialToolbar();
    }

    interface Presenter extends CustomerPresenter<View> {

        void initialize();

        void actionCheckPendingRequestIfAny();
    }
}
