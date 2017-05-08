package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetCurrentRideRequestUseCase;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/21/17.
 */

public class RideHomePresenter extends BaseDaggerPresenter<RideHomeContract.View> implements RideHomeContract.Presenter {
    private GetCurrentRideRequestUseCase mGetCurrentRideRequestUseCase;

    public RideHomePresenter(GetCurrentRideRequestUseCase getCurrentRideRequestUseCase) {
        this.mGetCurrentRideRequestUseCase = getCurrentRideRequestUseCase;
    }

    @Override
    public void initialize() {
        if (getView().isHavePendingRequestAndOpenedFromPushNotif()) {
            getView().closeScreen();
        } else {
            actionCheckPendingRequestIfAny();
        }
    }

    @Override
    public void actionCheckPendingRequestIfAny() {
        getView().showCheckPendingRequestLoading();
        mGetCurrentRideRequestUseCase.execute(getView().getCurrentRideRequestParam(), new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideCheckPendingRequestLoading();
                    getView().showRetryCheckPendingRequestLayout();
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()) {
                    if (rideRequest != null) {
                        getView().hideCheckPendingRequestLoading();
                        switch (rideRequest.getStatus()){
                            case RideStatus.ACCEPTED:
                            case RideStatus.ARRIVING:
                            case RideStatus.IN_PROGRESS:
                            case RideStatus.PROCESSING:
                                getView().actionNavigateToOnTripScreen(rideRequest);
                                break;
                            default:
                                getView().inflateInitialFragment();
                        }

                    } else {
                        getView().hideCheckPendingRequestLoading();
                        getView().inflateInitialFragment();
                    }
                }
            }
        });
    }

    @Override
    public void initialize() {
        if (getView().isUserLoggedIn()) {
            if (getView().isUserPhoneNumberVerified()) {
                getView().inflateMapAndProductFragment();
            } else {
                getView().showVerificationPhoneNumberPage();
            }
        } else {
            getView().navigateToLoginPage();
        }
    }
}
