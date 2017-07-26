package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetCurrentRideRequestUseCase;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.common.ride.domain.model.RideRequestAddress;
import com.tokopedia.ride.ontrip.view.viewmodel.DriverVehicleAddressViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/21/17.
 */

public class RideHomePresenter extends BaseDaggerPresenter<RideHomeContract.View> implements RideHomeContract.Presenter {
    private GetCurrentRideRequestUseCase getCurrentRideRequestUseCase;

    @Inject
    public RideHomePresenter(GetCurrentRideRequestUseCase getCurrentRideRequestUseCase) {
        this.getCurrentRideRequestUseCase = getCurrentRideRequestUseCase;
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
        getView().hideMainLayout();
        getCurrentRideRequestUseCase.execute(getView().getCurrentRideRequestParam(), new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideCheckPendingRequestLoading();
                    getView().inflateMapAndProductFragment();
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
                                getView().showMainLayout();
                                getView().actionNavigateToOnTripScreen(rideRequest);
                                break;
                            case RideStatus.DRIVER_CANCELED:
                                getView().showMainLayout();
                                // if user didnt see about driver canceled his ride
                                if (getView().getLastRequestId().equalsIgnoreCase(rideRequest.getRequestId())) {
                                    getView().showDialogDriverCancelled();
                                    getView().inflateMapAndProductFragment();
                                } else {
                                    getView().inflateMapAndProductFragment();
                                }
                                getView().clearActiveRequestId();
                                break;
                            case RideStatus.COMPLETED:
                                // if user didnt see last trip thanks page
                                if (getView().getLastRequestId().equalsIgnoreCase(rideRequest.getRequestId())
                                        && rideRequest.getPayment() != null
                                        && rideRequest.getPayment().isReceiptReady()) {
                                    DriverVehicleAddressViewModel driverAndVehicle = new DriverVehicleAddressViewModel();
                                    driverAndVehicle.setDriver(rideRequest.getDriver());
                                    driverAndVehicle.setVehicle(rideRequest.getVehicle());
                                    RideRequestAddress rideRequestAddress = new RideRequestAddress();
                                    rideRequestAddress.setStartAddressName(rideRequest.getPickup().getAddressName());
                                    rideRequestAddress.setStartAddress(rideRequest.getPickup().getAddress());
                                    rideRequestAddress.setEndAddressName(rideRequest.getDestination().getAddressName());
                                    rideRequestAddress.setEndAddress(rideRequest.getDestination().getAddress());
                                    driverAndVehicle.setAddress(rideRequestAddress);
                                    getView().navigateToCompleteTripScreen(rideRequest.getRequestId(), driverAndVehicle);
                                } else {
                                    getView().clearActiveRequestId();
                                    getView().showMainLayout();
                                    getView().hideCheckPendingRequestLoading();
                                    getView().inflateMapAndProductFragment();
                                }
                                break;
                            default:
                                getView().clearActiveRequestId();
                                getView().showMainLayout();
                                getView().inflateMapAndProductFragment();
                        }

                    } else {
                        getView().showMainLayout();
                        getView().hideCheckPendingRequestLoading();
                        getView().inflateMapAndProductFragment();
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        detachView();
        getCurrentRideRequestUseCase.unsubscribe();
    }


}
