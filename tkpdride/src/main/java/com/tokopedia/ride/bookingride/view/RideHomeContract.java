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

        void inflateMapAndProductFragment();
        void showCheckPendingRequestLoading();

        RequestParams getCurrentRideRequestParam();

        void hideCheckPendingRequestLoading();

        void showRetryCheckPendingRequestLayout();

        void actionNavigateToOnTripScreen(RideRequest rideRequest);

        void inflateInitialFragment();

        boolean isHavePendingRequestAndOpenedFromPushNotif();

        void closeScreen();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void actionCheckPendingRequestIfAny();
    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
