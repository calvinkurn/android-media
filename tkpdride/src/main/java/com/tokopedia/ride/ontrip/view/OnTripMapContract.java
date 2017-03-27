package com.tokopedia.ride.ontrip.view;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.ConfirmBookingViewModel;

/**
 * Created by alvarisi on 3/24/17.
 */

public interface OnTripMapContract {
    interface View extends CustomerView{

        RequestParams getParam();

        boolean isWaitingResponse();

        void showLoadingWaitingResponse();

        void hideLoadingWaitingResponse();

        void showFailedRideRequestMessage(String message);

        Activity getActivity();

        void showMessage(String message);

        void moveToCurrentLocation(double latitude, double longitude);

        RequestParams getCancelParams();

        void hideCancelRequestButton();

        void showCancelRequestButton();

        void navigateToBack();
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void goToMyLocation();

        void actionCancelRide();
    }
}
