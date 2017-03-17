package com.tokopedia.ride.bookingride.view;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.location.places.Place;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

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
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void getAvailableProducts();

        void onMapMoveCameraStarted();

        void onMapMoveCameraIdle();
    }
}
