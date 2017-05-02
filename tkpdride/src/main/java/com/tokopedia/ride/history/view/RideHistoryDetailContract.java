package com.tokopedia.ride.history.view;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideHistoryDetailContract {

    interface View extends CustomerView {

        void renderHistory();

        void showMainLayout();

        Context getActivity();

        void setPickupLocationText(String sourceAddress);

        void setDestinationLocation(String sourceAddress);

        RideHistoryViewModel getRideHistory();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
