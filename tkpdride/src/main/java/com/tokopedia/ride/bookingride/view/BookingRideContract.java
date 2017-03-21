package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.List;

/**
 * Created by alvarisi on 3/13/17.
 */

public interface BookingRideContract {
    interface View extends CustomerView {
        Context getActivityContext();

        boolean isUserLoggedIn();

        void navigateToLoginPage();

        void showVerificationPhoneNumberPage();

        boolean isUserPhoneNumberVerified();

        void prepareMainView();

        Activity getActivity();

        void showMessage(String message);

        void setSourceLocationText(String address);

        void setDestinationLocationText(String address);

        void moveToCurrentLocation(double latitude, double longitude);

        void renderDefaultPickupLocation(double latitude, double longitude);

        void onMapDragStarted();

        void onMapDragStopped();

        void renderTripRoute(List<List<LatLng>> routes);

        void setSourceLocation(Location location);

        void hideMarkerCenter();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void getAvailableProducts();

        void onMapMoveCameraStarted();

        void onMapMoveCameraIdle();

        void getOverviewPolyline(double sourceLat, double sourceLng,
                                 double destinationLat, double destinationLng);

        void actionMyLocation();
    }
}
