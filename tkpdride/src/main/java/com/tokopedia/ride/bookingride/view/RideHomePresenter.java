package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetCurrentRideRequestUseCase;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.exception.UnProcessableHttpException;
import com.tokopedia.ride.common.exception.UnprocessableEntityHttpException;
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
        if (getView().isUserLoggedIn()) {
            if (getView().isUserPhoneNumberVerified()) {
                if (getView().isHavePendingRequestAndOpenedFromPushNotif()) {
                    getView().closeScreen();
                } else {
                    getView().actionInflateInitialToolbar();
                    actionCheckPendingRequestIfAny();
                }
            } else {
                getView().showVerificationPhoneNumberPage();
            }
        } else {
            getView().navigateToLoginPage();
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
//                    getView().hideCheckPendingRequestLoading();
                    getView().hideCheckPendingRequestLoading();
                    getView().inflateMapAndProductFragment();
//                    if (e instanceof UnprocessableEntityHttpException) {
//                        getView().hideCheckPendingRequestLoading();
//                        getView().showRetryCheckPendingRequestLayout(e.getMessage());
//                    } else if (e instanceof UnProcessableHttpException) {
//                        getView().hideCheckPendingRequestLoading();
//                        getView().showRetryCheckPendingRequestLayout(e.getMessage());
//                    } else {
////                        getView().hideCheckPendingRequestLoading();
////                        getView().showRetryCheckPendingRequestLayout();
//                        getView().hideCheckPendingRequestLoading();
//                        getView().inflateMapAndProductFragment();
//                    }
                }

            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()) {
                    if (rideRequest != null) {
                        getView().hideCheckPendingRequestLoading();
                        switch (rideRequest.getStatus()) {
                            case RideStatus.ACCEPTED:
                            case RideStatus.ARRIVING:
                            case RideStatus.IN_PROGRESS:
                            case RideStatus.PROCESSING:
                                getView().actionNavigateToOnTripScreen(rideRequest);
                                break;
                            default:
                                getView().inflateMapAndProductFragment();
                        }

                    } else {
                        getView().hideCheckPendingRequestLoading();
                        getView().inflateMapAndProductFragment();
                    }
                }
            }
        });
    }


}
