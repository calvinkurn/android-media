package com.tokopedia.ride.ontrip.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.ride.domain.model.GetPending;
import com.tokopedia.ride.common.ride.domain.model.PendingPayment;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/24/17.
 */

public interface OnTripMapContract {
    interface View extends CustomerView {

        RequestParams getParam();

        RequestParams getFareEstimateParam();

//        boolean isWaitingResponse();

        void reDrawDriverMarker(double latitude, double longitude, float bearing);

        void hideRideRequestStatus();

        void showRequestRideStatus(String message);

        void showLoadingWaitingResponse();

        void hideLoadingWaitingResponse();

        void showFailedRideRequestMessage(String message);

        Activity getActivity();

        void showMessage(String message);

        void moveToCurrentLocation(double latitude, double longitude);

        RequestParams getPolyLineParam(double driverlat, double driverLon);

        RequestParams getCancelParams();

        void hideCancelRequestButton();

        void showCancelRequestButton();

        void openInterruptConfirmationWebView(String tosUrl);

        void failedToRequestRide(String message);

        void startPeriodicService(String requestId);

        String getRequestId();

        RideRequest getRideRequest();

        void onSuccessCreateRideRequest(RideRequest rideRequest);

        void showBottomSection();

        void renderAcceptedRequest(RideRequest result);

        void renderInProgressRequest(RideRequest result);

        void renderDriverCanceledRequest(RideRequest result);

        void renderCompletedRequest(RideRequest result);

//        void clearRideConfiguration();

        void renderArrivingDriverEvent(RideRequest result);

        void setTitle(int resId);

        void setTitle(String title);

        void onSuccessCancelRideRequest();

        void renderTripRoute(List<List<LatLng>> routes);

        void renderRiderCanceledRequest(RideRequest result);

        void showNoDriverAvailableDialog();

        void showFindingUberNotification();

        void hideFindingUberNotification();

        void showAcceptedNotification(RideRequest result);

        void hideAcceptedNotification();

        void updateDriverBitmapInNotification(RemoteViews remoteView, Bitmap bitmap);

        RequestParams getShareEtaParam();

        void showFailedShare();

        void showShareDialog(String shareUrl);

        void hideCancelPanel();

        RequestParams getCurrentRequestParams(String requestId);

        void clearActiveNotification();

        String getResourceString(int resourceId);

        boolean isSurge();

        String getSurgeConfirmationHref();

        boolean isAlreadyRequested();

        void setSourceAndDestinationTextByBookingParam();

        void setRequestId(String requestId);

        void setMapViewListener();

        void renderTripRouteWithoutAnimation(List<List<LatLng>> routes);

        void renderSourceMarker(double latitude, double longitude);

        void renderDestinationMarker(double latitude, double longitude);

        void openSmsIntent(String smsNumber);

        void setAddressPickerText(String startAddressName, String endAddressName);

        void checkAndExecuteCallPermission(String phoneNumber);

        void showBlockTranslucentLayout();

        void hideBlockTranslucentLayout();

        void moveMapToLocation(double latitude, double longitude);

        boolean isAlreadyRouteDrawed();

        void updateSourceCoordinate(double latitude, double longitude);

        void updateDestinationCoordinate(double latitude, double longitude);

        RequestParams getProductDetailParam(String productId);

        void setDriverIcon(RideRequest rideRequest, int drawable);

        void saveActiveRequestId(String requestId);

        void clearSavedActiveRequestId();

        void saveActiveProductName();

        void clearSavedActiveProductName();

        String getActiveProductNameInCache();

        void saveActiveProductName(String displayName);

        void saveCancellationFee(String cancellationFee);

        String getCancellationFee();

        void showShareEtaProgress();

        void hideShareEtaProgress();

        void showCurrentLocationIndicator();

        void hideCurrentLocationIndicator();

        RequestParams getPolyLineParamDriverBetweenDestination(double latitude, double longitude);

        void zoomMapFitByPolyline(List<LatLng> latLngs);

        void actionNavigateToCancelReasonPage(String requestId, String timestamp);


        void showCancellationLayout();

        void actionNavigateToCancelReasonPage(String requestId);

        void zoomMapFitByDriverAndCustomer(List<LatLng> latLngs);

        void openInterruptConfirmationDialog(String tosUrl, String key, String value);

        void saveDefaultLocation(double latitude, double longitude);

        void setDestination(PlacePassViewModel destinationTemp);

        void showUpdateDestinationLoading();

        void hideUpdateDestinationLoading();

        void startTopupTokoCashChangeDestinationActivity(PendingPayment pendingPayment, String requestId);

        void renderCompletedRequestWithoutReceipt(RideRequest result);

        void showPendingFareInterrupt(GetPending getPending);
    }

    interface Presenter extends CustomerPresenter<View> {

        void initialize();

        /**
         * This function handles an action to create ride request. It makes composite call, first for fare-estimate  and then uber/request
         *
         * @param requestParam, parameters required for ride request
         */
        void actionRideRequest(RequestParams requestParam);

        void proccessGetCurrentRideRequest(RideRequest result);

        void getDriverBitmap(RemoteViews remoteView, String imgUrl);

        void getOverViewPolyLine(boolean animate, boolean zoomToFit);

        void actionShareEta();

        void startGetRequestDetailsPeriodicService(String requestId);

        void actionOnReceivePushNotification(Observable<RideRequest> rideRequest);

        void onMapReady();

        void actionCallDriver();

        void actionMessageDriver();

        boolean checkIsAnyPendingRequest();

        void actionGoToCurrentLocation();

        void onResume();

        void onPause();

        void actionYesCancelBtnClicked();

        String getRideStatus();

        void actionCancelButtonClicked();

        String getDeviceName();

        void updateDestination(PlacePassViewModel destinationTemp);
    }
}
