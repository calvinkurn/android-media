package com.tokopedia.ride.bookingride.view;

import android.app.Activity;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.bookingride.view.viewmodel.PlacePassViewModel;

import java.util.ArrayList;

/**
 * Created by alvarisi on 3/15/17.
 */

public interface PlaceAutoCompleteContract {
    interface View extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void showAutoCompleteLoadingCross();

        void hideAutoCompleteLoadingCross();

        void showAutoDetectLocationButton();

        void hideAutoDetectLocationButton();

        void showHomeLocationButton();

        void hideHomeLocationButton();

        void showWorkLocationButton();

        void hideWorkLocationButton();

        void renderPlacesList(ArrayList<Visitable> visitables);

        void resetSearch();

        void onPlaceSelectedFound(PlacePassViewModel placePassViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void actionQueryPlacesByKeyword(String keyword);

        void actionAutoDetectLocation();

        void actionHomeLocation();

        void actionWorkLocation();

        void onDestroy();

        void onStop();

        void onStart();

        void onPlaceSelected(String addressId);
    }
}
