package com.tokopedia.ride.history.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryDetailPresenter extends BaseDaggerPresenter<RideHistoryDetailContract.View> implements RideHistoryDetailContract.Presenter {
    public RideHistoryDetailPresenter(GetSingleRideHistoryUseCase getSingleRideHistoryUseCase, GetOverviewPolylineUseCase getOverviewPolylineUseCase) {
    }

    @Override
    public void initialize() {

    }
}
