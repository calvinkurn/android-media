package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetCurrentRideRequestUseCase;

/**
 * Created by alvarisi on 4/21/17.
 */

public class RideHomePresenter extends BaseDaggerPresenter<RideHomeContract.View> implements RideHomeContract.Presenter {
    private GetCurrentRideRequestUseCase mGetCurrentRideRequestUseCase;

    public RideHomePresenter(GetCurrentRideRequestUseCase getCurrentRideRequestUseCase) {
        this.mGetCurrentRideRequestUseCase = getCurrentRideRequestUseCase;
    }
}
