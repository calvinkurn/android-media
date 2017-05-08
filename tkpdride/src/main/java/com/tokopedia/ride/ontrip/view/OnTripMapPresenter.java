package com.tokopedia.ride.ontrip.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.RemoteViews;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.maps.android.PolyUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.exception.InterruptConfirmationHttpException;
import com.tokopedia.ride.common.exception.UnprocessableEntityHttpException;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 3/24/17.
 */

public class OnTripMapPresenter extends BaseDaggerPresenter<OnTripMapContract.View>
        implements OnTripMapContract.Presenter {

    private static final long CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY = 3000;

    private CreateRideRequestUseCase createRideRequestUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetRideRequestMapUseCase getRideRequestMapUseCase;
    private GetRideRequestDetailUseCase getRideRequestUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private RideRequest activeRideRequest;

    private Handler handler = new Handler();

    public OnTripMapPresenter(CreateRideRequestUseCase createRideRequestUseCase,
                              CancelRideRequestUseCase cancelRideRequestUseCase,
                              GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                              GetRideRequestMapUseCase getRideRequestMapUseCase,
                              GetRideRequestDetailUseCase getRideRequestUseCase,
                              GetFareEstimateUseCase getFareEstimateUseCase) {
        this.createRideRequestUseCase = createRideRequestUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getRideRequestMapUseCase = getRideRequestMapUseCase;
        this.getRideRequestUseCase = getRideRequestUseCase;
        this.getFareEstimateUseCase = getFareEstimateUseCase;
    }


    @Override
    public void initialize() {
        getView().showLoadingWaitingResponse();
        getView().hideRideRequestStatus();
        getView().hideCancelRequestButton();

        if (getView().isAlreadyRequested()) {
            getView().startPeriodicService(getView().getRequestId());
        } else {
            getView().setViewListener();
            if (getView().isSurge()) {
                getView().openInterruptConfirmationWebView(getView().getSurgeConfirmationHref());
            } else {
                actionRideRequest(getView().getParam());
            }
        }
    }

    /**
     * This function combines first checks the fare estimate again and then make a create ride request with latest fare id.
     *
     * @param requestParam, parameters required for ride request
     */
    @Override
    public void actionRideRequest(final RequestParams requestParam) {
        getView().showRequestLoadingLayout();
        getFareEstimateUseCase.execute(getView().getFareEstimateParam(), new Subscriber<FareEstimate>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().hideRequestLoadingLayout();
                getView().showFailedRideRequestMessage(e.getMessage());
                getView().failedToRequestRide(e.getMessage());
            }

            @Override
            public void onNext(FareEstimate fareEstimate) {
                if (fareEstimate.getFare() != null) {
                    //update fare id with latest fare id
                    requestParam.putString(CreateRideRequestUseCase.PARAM_FARE_ID, fareEstimate.getFare().getFareId());
                    createRideRequest(requestParam);
                } else if (fareEstimate.getEstimate() != null) {
                    //open surge confirmation webview
                    getView().openInterruptConfirmationWebView(getView().getSurgeConfirmationHref());
                }
            }
        });
    }

    public void createRideRequest(RequestParams requestParams) {
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (!isViewAttached()) return;
                getView().hideRequestLoadingLayout();
                if (e instanceof InterruptConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openInterruptConfirmationWebView(((InterruptConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().showFailedRideRequestMessage(e.getMessage());
                        getView().failedToRequestRide(e.getMessage());
                    }
                    ///getView().hideLoadingWaitingResponse();
                    //getView().hideRideRequestStatus();
                } else if (e instanceof UnprocessableEntityHttpException) {
                    //get fare id again
                    getView().showFailedRideRequestMessage(getView().getResourceString(R.string.error_invalid_fare_id));
                    getView().failedToRequestRide(getView().getResourceString(R.string.error_invalid_fare_id));
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide(e.getMessage());
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()) {
                    getView().hideRequestLoadingLayout();
                    activeRideRequest = rideRequest;
                    getView().setRequestId(rideRequest.getRequestId());
                    getView().onSuccessCreateRideRequest(rideRequest);
                    getView().startPeriodicService(rideRequest.getRequestId());
                    proccessGetCurrentRideRequest(rideRequest);
                }
            }
        });
    }

    @Override
    public void actionCancelRide() {
        cancelRideRequestUseCase.execute(getView().getCancelParams(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showMessage(e.getMessage());
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().onSuccessCancelRideRequest();
                    getView().clearActiveNotification();
                }
            }
        });

    }

    @Override
    public void proccessGetCurrentRideRequest(RideRequest result) {
        getView().setRequestId(result.getRequestId());
        if (result.getAddress() != null) {
            getView().setAddressPickerText(result.getAddress().getStartAddressName(), result.getAddress().getEndAddressName());
        }
        //processing accepted arriving in_progress driver_canceled completed
        switch (result.getStatus()) {
            case RideStatus.NO_DRIVER_AVAILABLE:
                getView().hideFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().clearRideConfiguration();
                getView().showNoDriverAvailableDialog();
                break;
            case RideStatus.PROCESSING:
                getView().showFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().showCancelRequestButton();
                break;
            case RideStatus.ACCEPTED:
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showAcceptedNotification(result);
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderAcceptedRequest(result);
                getView().showBottomSection();
                break;
            case RideStatus.ARRIVING:
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderAcceptedRequest(result);
                getView().renderArrivingDriverEvent(result);
                break;
            case RideStatus.IN_PROGRESS:
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderInProgressRequest(result);
                getView().showRequestRideStatus(String.format("On Trip", String.valueOf(result.getDestination().getEta())));
                break;
            case RideStatus.DRIVER_CANCELED:
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderDriverCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case RideStatus.RIDER_CANCELED:
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderRiderCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case RideStatus.COMPLETED:
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderCompletedRequest(result);
                getView().clearRideConfiguration();
                break;
            default:

        }
    }

    @Override
    public void getOverViewPolyLine() {
        getOverviewPolylineUseCase.execute(getView().getPolyLineParam(), new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                if (isViewAttached()) {
                    List<List<LatLng>> routes = new ArrayList<>();
                    for (String route : strings) {
                        routes.add(PolyUtil.decode(route));
                    }
                    getView().renderTripRoute(routes);
                    if (activeRideRequest != null) {
                        getView().renderSourceMarker(activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude());
                        getView().renderDestinationMarker(activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude());
                        getView().zoomMapFitWithSourceAndDestination(
                                activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude(),
                                activeRideRequest.getPickup().getLatitude(),
                                activeRideRequest.getDestination().getLongitude()
                        );
                    }
                }
            }
        });
    }

    @Override
    public void actionShareEta() {
        getRideRequestMapUseCase.execute(getView().getShareEtaParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showFailedShare();
                }
            }

            @Override
            public void onNext(String shareUrl) {
                if (isViewAttached()) {
                    getView().showShareDialog(shareUrl);
                }
            }
        });
    }


    /**
     * This is a task to poll the request details api after every 2 seconds
     */
    private Runnable timedTask = new Runnable() {
        @Override
        public void run() {

            //if get view is null
            if (getView() == null) {
                if (handler != null) {
                    handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
                }

                return;
            }


            //request the current ride details
            getRideRequestUseCase.execute(getView().getCurrentRequestParams(getView().getRequestId()),
                    new Subscriber<RideRequest>() {
                        @Override
                        public void onCompleted() {
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask complete");
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask error = " + e.toString());
                        }

                        @Override
                        public void onNext(RideRequest rideRequest) {
                            CommonUtils.dumper("GetCurrentRideRequestService timedTask onNext");

                            //return of fragment finished on not present
                            if (getView() == null) {
                                return;
                            }
                            activeRideRequest = rideRequest;

                            proccessGetCurrentRideRequest(rideRequest);
                            handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
                        }
                    });
        }
    };

    @Override
    public void startGetRequestDetailsPeriodicService(String requestId) {
        handler.postDelayed(timedTask, 2000);
    }

    @Override
    public void actionOnReceivePushNotification(Observable<RideRequest> rideObservable) {
        rideObservable.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RideRequest>() {
                    @Override
                    public void call(RideRequest rideRequest) {
                        if (isViewAttached()) {
                            activeRideRequest = rideRequest;
                            proccessGetCurrentRideRequest(rideRequest);
                        }
                    }
                });
    }

    @Override
    public void getDriverBitmap(final RemoteViews remoteView, final String imgUrl) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap bm = null;
                try {
                    URL aURL = new URL(imgUrl);
                    URLConnection conn = aURL.openConnection();
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                subscriber.onNext(bm);

            }
        })
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        if (isViewAttached()) {
                            getView().updateDriverBitmapInNotification(remoteView, bitmap);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        cancelRideRequestUseCase.unsubscribe();
        createRideRequestUseCase.unsubscribe();
        getOverviewPolylineUseCase.unsubscribe();
        getRideRequestMapUseCase.unsubscribe();
        super.detachView();
    }

    @Override
    public void onMapReady() {
        getView().setMapViewListener();
        if (!getView().isAlreadyRequested()) {
            getOverViewPolyLine();
        }
    }

    @Override
    public void actionCallDriver() {
        getView().checkAndExecuteCallPermission(activeRideRequest.getDriver().getPhoneNumber());
    }

    @Override
    public void actionMessageDriver() {
        getView().openSmsIntent(activeRideRequest.getDriver().getSmsNumber());
    }

    @Override
    public boolean checkIsAnyPendingRequest() {
        return activeRideRequest != null;
    }
}
