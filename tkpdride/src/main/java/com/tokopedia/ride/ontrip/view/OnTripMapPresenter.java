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
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.common.exception.TosConfirmationHttpException;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetCurrentDetailRideRequestUseCase;
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
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 3/24/17.
 */

public class OnTripMapPresenter extends BaseDaggerPresenter<OnTripMapContract.View>
        implements OnTripMapContract.Presenter {

    public static final long CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY = 3000;

    private CreateRideRequestUseCase createRideRequestUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetRideRequestMapUseCase getRideRequestMapUseCase;
    private GetCurrentDetailRideRequestUseCase getRideRequestUseCase;

    private Handler handler = new Handler();
    private String mRequestId;

    public OnTripMapPresenter(CreateRideRequestUseCase createRideRequestUseCase,
                              CancelRideRequestUseCase cancelRideRequestUseCase,
                              GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                              GetRideRequestMapUseCase getRideRequestMapUseCase,
                              GetCurrentDetailRideRequestUseCase getRideRequestUseCase) {
        this.createRideRequestUseCase = createRideRequestUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getRideRequestMapUseCase = getRideRequestMapUseCase;
        this.getRideRequestUseCase = getRideRequestUseCase;
    }


    @Override
    public void initialize() {
        getView().showLoadingWaitingResponse();
        getView().hideRideRequestStatus();
        getView().hideCancelRequestButton();

        if (getView().isWaitingResponse()) {
            getView().startPeriodicService(getView().getRequestId());
        } else {
            actionRideRequest(getView().getParam());
        }
    }


    private void actionRideRequest(RequestParams requestParams) {
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

                if (!isViewAttached()) return;

                if (e instanceof TosConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openTosConfirmationWebView(((TosConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().failedToRequestRide();
                    }
                    getView().hideLoadingWaitingResponse();
                    getView().hideRideRequestStatus();
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide();
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()){
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
                }
            }
        });

    }

    @Override
    public void actionRetryRideRequest(String id) {
        RequestParams requestParams = getView().getParam();
        requestParams.putString("tos_confirmation_id", id);
        createRideRequestUseCase.execute(requestParams, new Subscriber<RideRequest>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (!isViewAttached()) return;
                if (e instanceof TosConfirmationHttpException) {
                    if (!(e.getCause() instanceof JsonSyntaxException)) {
                        getView().openTosConfirmationWebView(((TosConfirmationHttpException) e).getTosUrl());
                    } else {
                        getView().failedToRequestRide();
                    }
                    getView().hideLoadingWaitingResponse();
                    getView().hideRideRequestStatus();
                } else {
                    getView().showFailedRideRequestMessage(e.getMessage());
                    getView().failedToRequestRide();
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()){
                    proccessGetCurrentRideRequest(rideRequest);
                    getView().onSuccessCreateRideRequest(rideRequest);
                }
            }
        });
    }

    @Override
    public void proccessGetCurrentRideRequest(RideRequest result) {
        //processing accepted arriving in_progress driver_canceled completed
        switch (result.getStatus()) {
            case "no_drivers_available":
                getView().hideFindingUberNotification();
                getView().hideFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().clearRideConfiguration();
                getView().showNoDriverAvailableDialog();
                break;
            case "processing":
                getView().showFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().showCancelRequestButton();
                break;
            case "accepted":
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showAcceptedNotification(result);
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderAcceptedRequest(result);
                getView().showBottomSection();
                break;
            case "arriving":
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().showRequestRideStatus(String.format("Driver will pick in %s minutes", String.valueOf(result.getPickup().getEta())));
                getView().renderAcceptedRequest(result);
                getView().renderArrivingDriverEvent(result);
                break;
            case "in_progress":
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderInProgressRequest(result);
                getView().showRequestRideStatus(String.format("Will arrive to destination in %s minutes", String.valueOf(result.getDestination().getEta())));
                break;
            case "driver_canceled":
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderDriverCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case "rider_canceled":
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderRiderCanceledRequest(result);
                getView().clearRideConfiguration();
                break;
            case "completed":
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderCompletedRequest(result);
                getView().clearRideConfiguration();
                break;
            default:

        }
    }

    @Override
    public void getOverViewPolyLine(double startLat, double startLng, double destLat, double destLng) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("origin", String.format("%s,%s",
                startLat,
                startLng
        ));
        requestParams.putString("destination", String.format("%s,%s",
                destLat,
                destLng
        ));
        requestParams.putString("sensor", "false");
        getOverviewPolylineUseCase.execute(requestParams, new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<String> strings) {
                if (isViewAttached()){
                    List<List<LatLng>> routes = new ArrayList<>();
                    for (String route : strings) {
                        routes.add(PolyUtil.decode(route));
                    }
                    getView().renderTripRoute(routes);
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
            if (getView() == null) {
                return;
            }


            //request the current ride details
            getRideRequestUseCase.execute(getView().getCurrentRequestParams(mRequestId), new Subscriber<RideRequest>() {
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
                public void onNext(RideRequest s) {
                    CommonUtils.dumper("GetCurrentRideRequestService timedTask onNext");

                    //return of fragment finished on not present
                    if (getView() == null) {
                        return;
                    }

                    proccessGetCurrentRideRequest(s);
                    handler.postDelayed(timedTask, CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY);
                }
            });
        }
    };

    @Override
    public void startGetRequestDetailsPeriodicService(String requestId) {
        this.mRequestId = requestId;
        handler.postDelayed(timedTask, 2000);
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
}
