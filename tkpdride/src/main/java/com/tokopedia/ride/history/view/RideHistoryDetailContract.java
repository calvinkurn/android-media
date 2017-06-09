package com.tokopedia.ride.history.view;

import android.content.Context;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.ride.history.domain.model.RideHistory;
import com.tokopedia.ride.history.view.viewmodel.RideHistoryViewModel;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideHistoryDetailContract {

    interface View extends CustomerView {

        void renderHistory(RideHistoryViewModel rideHistoryViewModel);

        void showHistoryDetailLayout();

        Context getActivity();

        void setPickupLocationText(String sourceAddress);

        void setDestinationLocation(String sourceAddress);

        RideHistoryViewModel getRideHistory();

        boolean isRatingAvailable();

        void showRatingLayout();

        void hideRatingLayout();

        RequestParams getRatingParam();

        void showSuccessRatingDialog();

        void hideMainLayout();

        void showLoading();

        void showMainLayout();

        void hideLoading();

        void showRatingNetworkError();

        void renderSuccessfullGiveRating(int star);

        RequestParams getSingleHistoryParam();

        void showErrorLayout();


        String getMapKey();

        String getMapSize();

        void setHistoryViewModelData(RideHistoryViewModel viewModel);

        int getRateStars();
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();

        void actionSendRating();
    }
}
