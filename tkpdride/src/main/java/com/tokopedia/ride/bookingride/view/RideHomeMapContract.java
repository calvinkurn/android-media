package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.domain.model.NearbyRides;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.List;

/**
 * Created by alvarisi on 3/13/17.
 */

public interface RideHomeMapContract {
    interface View extends CustomerView {
        Context getActivityContext();

        boolean isUserLoggedIn();

        void navigateToLoginPage();

        void showVerificationPhoneNumberPage();

        boolean isUserPhoneNumberVerified();

        void prepareMainView();

        Activity getActivity();

        void showMessage(String message, String btntext);

        void setSourceLocationText(String address);

        void setDestinationLocationText(String address);

        void moveMapToLocation(double latitude, double longitude);

        void renderDefaultPickupLocation(double latitude, double longitude, String title, String sourceAddress);

        void onMapDragStarted();

        void onMapDragStopped();

        void renderTripPolyline(List<OverviewPolyline> overviewPolylines);

        void setSourceLocation(PlacePassViewModel location);

        void hideMarkerCenter();

        void showMarkerCenter();

        void showEnterDestError();

        boolean isAlreadySelectDestination();

        boolean isLaunchedWithLocation();

        void setDestinationAndProcessList(PlacePassViewModel address);

        PlacePassViewModel getSource();

        void renderNearbyRides(NearbyRides nearbyRides);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void onMapMoveCameraStarted();

        void onMapMoveCameraIdle();

        void getOverviewPolyline(double sourceLat, double sourceLng,
                                 double destinationLat, double destinationLng);

        void actionMyLocation();

        void handleEnableLocationDialogResult(int resultCode);

        void actionMapDragStopped(double latitude, double longitude);

        void appResumedFromBackground();

        void setSourceSelectedFromAddress();
    }
}
