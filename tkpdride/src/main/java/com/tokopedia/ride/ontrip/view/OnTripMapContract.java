package com.tokopedia.ride.ontrip.view;

import android.app.Activity;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

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

        void openTosConfirmationWebView(String tosUrl);

        void failedToRequestRide();

        void startPeriodicService(String requestId);

        String getRequestId();

        void onSuccessCreateRideRequest(RideRequest rideRequest);

        void renderAcceptedRequest(RideRequest result);

        void renderInProgressRequest(RideRequest result);

        void renderDriverCanceledRequest(RideRequest result);

        void renderCompletedRequest(RideRequest result);
    }

    interface Presenter extends CustomerPresenter<View>{

        void initialize();

        void goToMyLocation();

        void actionCancelRide();

        void actionRetryRideRequest(String id);

        void proccessGetCurrentRideRequest(RideRequest result);
    }
}
