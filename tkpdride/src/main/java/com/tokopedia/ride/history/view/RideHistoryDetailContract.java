package com.tokopedia.ride.history.view;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.history.domain.model.RideHistory;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideHistoryDetailContract {

    interface View extends CustomerView {

        RequestParams getSingleHistoryParam();

        void showErrorLayout();

        void renderHistory(RideHistory rideHistory);

        void showProgressBar();

        void hideProgressBar();

        void hideMainLayout();

        void showMainLayout();

        Context getActivity();

        void setPickupLocationText(String sourceAddress);

        void setDestinationLocation(String sourceAddress);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
