package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

/**
 * Created by alvarisi on 3/15/17.
 */

public interface SelectLocationOnMapContract {
    interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void moveMapToLocation(double latitude, double longitude);

        void setSourceLocationText(String address);

        void setDestinationLocationText(String address);

        void setDestination(PlacePassViewModel destination);

        void onMapDraggedStopped();

        void showCrossLoading();

        void disableDoneButton();

        void hideCrossLoading();

        void enableDoneButton();

        PlacePassViewModel getDefaultLocation();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void onDestroy();

        void onStop();

        void onStart();

        void onDone();

        void onMapMoveCameraStarted();

        void onMapMoveCameraIdle();

        void actionMapDragStopped(double latitude, double longitude);
    }
}
