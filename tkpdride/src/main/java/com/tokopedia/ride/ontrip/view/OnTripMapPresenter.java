package com.tokopedia.ride.ontrip.view;

import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonSyntaxException;
import com.google.maps.android.PolyUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.exception.InterruptConfirmationHttpException;
import com.tokopedia.core.network.exception.model.UnprocessableEntityHttpException;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.domain.GetFareEstimateUseCase;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.bookingride.view.fragment.RideHomeMapFragment;
import com.tokopedia.ride.common.configuration.RideConfiguration;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;
import com.tokopedia.ride.common.ride.domain.model.FareEstimate;
import com.tokopedia.ride.common.ride.domain.model.Product;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
import com.tokopedia.ride.ontrip.domain.CancelRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.CreateRideRequestUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideProductUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestDetailUseCase;
import com.tokopedia.ride.ontrip.domain.GetRideRequestMapUseCase;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 3/24/17.
 */

public class OnTripMapPresenter extends BaseDaggerPresenter<OnTripMapContract.View>
        implements OnTripMapContract.Presenter {
    private static final String DATE_SERVER_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static long CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY = 4000;

    private CreateRideRequestUseCase createRideRequestUseCase;
    private CancelRideRequestUseCase cancelRideRequestUseCase;
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetRideRequestMapUseCase getRideRequestMapUseCase;
    private GetRideRequestDetailUseCase getRideRequestUseCase;
    private GetFareEstimateUseCase getFareEstimateUseCase;
    private GetRideProductUseCase getRideProductUseCase;
    private RideRequest activeRideRequest;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location currentLocation;
    private CompositeSubscription subscription;
    private boolean isZoomFitByDriverAndCustomer;

    public OnTripMapPresenter(CreateRideRequestUseCase createRideRequestUseCase,
                              CancelRideRequestUseCase cancelRideRequestUseCase,
                              GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                              GetRideRequestMapUseCase getRideRequestMapUseCase,
                              GetRideRequestDetailUseCase getRideRequestUseCase,
                              GetFareEstimateUseCase getFareEstimateUseCase,
                              GetRideProductUseCase getRideProductUseCase) {
        this.createRideRequestUseCase = createRideRequestUseCase;
        this.cancelRideRequestUseCase = cancelRideRequestUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.getRideRequestMapUseCase = getRideRequestMapUseCase;
        this.getRideRequestUseCase = getRideRequestUseCase;
        this.getFareEstimateUseCase = getFareEstimateUseCase;
        this.getRideProductUseCase = getRideProductUseCase;
        this.subscription = new CompositeSubscription();
    }


    @Override
    public void initialize() {
        getView().showRequestLoadingLayout();
        getView().showLoadingWaitingResponse();
        getView().hideRideRequestStatus();
        getView().hideCancelRequestButton();

        if (getView().isAlreadyRequested()) {
            getView().setTitle(R.string.title_fetching_ride);

            if (getView().getRideRequest() != null) {
                handlePeriodicServiceSuccessResponse(getView().getRideRequest());
                activeRideRequest = getView().getRideRequest();
            }

            getView().startPeriodicService(getView().getRequestId());
        } else {
            getView().setSourceAndDestinationTextByBookingParam();
            if (getView().isSurge()) {
                getView().openInterruptConfirmationWebView(getView().getSurgeConfirmationHref());
            } else {
                actionRideRequest(getView().getParam());
            }
        }

        if (checkPlayServices()) {
            createLocationRequest();
            initializeLocationService();
        }
    }

    private void actionGetProductDetailToDecideIcon(final RideRequest rideRequest) {
        getRideProductUseCase.execute(getView().getProductDetailParam(rideRequest.getProductId()), new Subscriber<Product>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Product product) {
                if (isViewAttached()) {
                    getView().saveActiveProductName(product.getDisplayName());
                    if (product.getDisplayName().equalsIgnoreCase(getView().getActivity().getString(R.string.uber_moto_display_name))) {
                        getView().setDriverIcon(rideRequest, R.drawable.moto_map_icon);
                    } else {
                        getView().setDriverIcon(rideRequest, R.drawable.car_map_icon);
                    }
                }
            }
        });
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
                if (isViewAttached()) {
                    getView().hideRequestLoadingLayout();
                    String error;
                    if (e instanceof UnknownHostException) {
                        error = getView().getActivity().getString(R.string.error_no_connection);
                    } else if (e instanceof SocketTimeoutException) {
                        error = getView().getActivity().getString(R.string.error_timeout);
                    } else {
                        error = getView().getActivity().getString(R.string.error_default);
                    }
                    getView().failedToRequestRide(error);
                }
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

    private void createRideRequest(RequestParams requestParams) {
        if (currentLocation != null) {
            requestParams.putObject(CreateRideRequestUseCase.PARAM_APP_LATITUDE, currentLocation.getLatitude());
            requestParams.putObject(CreateRideRequestUseCase.PARAM_APP_LONGITUDE, currentLocation.getLongitude());
        }
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
                    String error;
                    if (e instanceof UnknownHostException) {
                        error = getView().getActivity().getString(R.string.error_no_connection);
                    } else if (e instanceof SocketTimeoutException) {
                        error = getView().getActivity().getString(R.string.error_timeout);
                    } else {
                        error = e.getMessage();
                    }
                    getView().failedToRequestRide(error);
                }
            }

            @Override
            public void onNext(RideRequest rideRequest) {
                if (isViewAttached()) {
                    activeRideRequest = rideRequest;
                    getView().setRequestId(rideRequest.getRequestId());
                    getView().saveActiveProductName();
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
                e.printStackTrace();
                if (isViewAttached()) {
                    if (e instanceof UnknownHostException) {
                        getView().showMessage(getView().getActivity().getString(R.string.error_no_connection));
                    } else if (e instanceof SocketTimeoutException) {
                        getView().showMessage(getView().getActivity().getString(R.string.error_timeout));
                    } else {
                        getView().showMessage(getView().getActivity().getString(R.string.error_default));
                    }
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().onSuccessCancelRideRequest();
                    getView().clearActiveNotification();
                    getView().clearSavedActiveRequestId();
                    getView().clearSavedActiveProductName();
                }
            }
        });

    }

    @Override
    public void proccessGetCurrentRideRequest(RideRequest result) {
        getView().setRequestId(result.getRequestId());
        actionSetAddressInPicker(result);
        switch (result.getStatus()) {
            case RideStatus.NO_DRIVER_AVAILABLE:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideLoadingWaitingResponse();
                getView().showNoDriverAvailableDialog();
                getView().clearSavedActiveRequestId();
                getView().clearSavedActiveProductName();
                break;
            case RideStatus.PROCESSING:
                RideConfiguration configuration = new RideConfiguration(getView().getActivity());
                if (!TextUtils.isEmpty(configuration.getActiveProductName())) {
                    getView().setTitle(String.format("%s %s",
                            getView().getActivity().getString(R.string.notification_title_finding_uber),
                            configuration.getActiveProductName())
                    );
                } else {
                    getView().setTitle(R.string.title_requesting_ride);
                }

                getView().showFindingUberNotification();
                getView().showLoadingWaitingResponse();
                getView().showCancelRequestButton();
                updatePolylineIfResetedByUiLifecycle(result);
                break;
            case RideStatus.ACCEPTED:
                getView().saveActiveRequestId(result.getRequestId());
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showAcceptedNotification(result);
                getView().renderAcceptedRequest(result);
                getView().showBottomSection();
                getView().showCurrentLocationIndicator();
                updatePolylineIfResetedByUiLifecycle(result);
                RideConfiguration configurationAcc = new RideConfiguration(getView().getActivity());
                if (!TextUtils.isEmpty(configurationAcc.getActiveProductName())) {
                    getView().setTitle(String.format("%s %s", configurationAcc.getActiveProductName(),
                            getView().getActivity().getString(R.string.title_trip_accepted_postfix))
                    );
                } else {
                    getView().setTitle(R.string.title_trip_accepted);
                }
                if (getView().isAlreadyRouteDrawed() && result.getPickup() != null && result.getLocation() != null && !isZoomFitByDriverAndCustomer) {
                    List<LatLng> latLngs = new ArrayList<>();
                    latLngs.add(new LatLng(result.getPickup().getLatitude(), result.getPickup().getLongitude()));
                    latLngs.add(new LatLng(result.getLocation().getLatitude(), result.getLocation().getLongitude()));
                    getView().zoomMapFitByDriverAndCustomer(latLngs);
                    isZoomFitByDriverAndCustomer = true;
                }
                break;
            case RideStatus.ARRIVING:
                getView().saveActiveRequestId(result.getRequestId());
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderAcceptedRequest(result);
                getView().renderArrivingDriverEvent(result);
                getView().showCurrentLocationIndicator();
                updatePolylineIfResetedByUiLifecycle(result);

                if (getView().isAlreadyRouteDrawed() && result.getPickup() != null && result.getLocation() != null && !isZoomFitByDriverAndCustomer) {
                    List<LatLng> latLngs = new ArrayList<>();
                    latLngs.add(new LatLng(result.getPickup().getLatitude(), result.getPickup().getLongitude()));
                    latLngs.add(new LatLng(result.getLocation().getLatitude(), result.getLocation().getLongitude()));
                    getView().zoomMapFitByDriverAndCustomer(latLngs);
                    isZoomFitByDriverAndCustomer = true;
                }

                break;
            case RideStatus.IN_PROGRESS:
                getView().saveActiveRequestId(result.getRequestId());
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().hideCancelRequestButton();
                getView().hideLoadingWaitingResponse();
                getView().showBottomSection();
                getView().renderAcceptedRequest(result);
                getView().renderInProgressRequest(result);
                getView().hideCurrentLocationIndicator();
                updatePolylineBetweenDriverAndDestination(result);
                break;
            case RideStatus.DRIVER_CANCELED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderDriverCanceledRequest(result);
                getView().clearSavedActiveRequestId();
                getView().clearSavedActiveProductName();
                break;
            case RideStatus.RIDER_CANCELED:
                getView().hideRequestLoadingLayout();
                getView().hideFindingUberNotification();
                getView().hideAcceptedNotification();
                getView().renderRiderCanceledRequest(result);
                getView().clearSavedActiveRequestId();
                getView().clearSavedActiveProductName();
                break;
            case RideStatus.COMPLETED:
                CommonUtils.dumper("proccessGetCurrentRideRequest## Ride Completed");
                if (result.getPayment() != null && result.getPayment().isReceiptReady()) {
                    CommonUtils.dumper("proccessGetCurrentRideRequest## Ride Completed ReceiptReady");
                    getView().hideRequestLoadingLayout();
                    getView().hideFindingUberNotification();
                    getView().hideAcceptedNotification();
                    getView().renderCompletedRequest(result);
                }
                break;
            default:
        }
    }

    private void updatePolylineBetweenDriverAndDestination(RideRequest result) {
        getView().updateSourceCoordinate(result.getPickup().getLatitude(), result.getPickup().getLongitude());
        getView().updateDestinationCoordinate(result.getDestination().getLatitude(), result.getDestination().getLongitude());
        boolean animation = true;
        if (getView().isAlreadyRouteDrawed()) {
            animation = false;
        }
        getOverViewPolyLineDriverBetweenDestination(animation, result);
    }

    private void getOverViewPolyLineDriverBetweenDestination(final boolean animation, RideRequest result) {
        RequestParams polylineRequestParams = getView().getPolyLineParamDriverBetweenDestination(result.getLocation().getLatitude(), result.getLocation().getLongitude());

        if (polylineRequestParams != null) {
            getOverviewPolylineUseCase.execute(polylineRequestParams, new Subscriber<List<OverviewPolyline>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<OverviewPolyline> overviewPolylines) {
                    if (isViewAttached()) {
                        List<List<LatLng>> routes = new ArrayList<>();
                        for (OverviewPolyline polyline : overviewPolylines) {
                            routes.add(PolyUtil.decode(polyline.getOverviewPolyline()));
                        }

                        if (animation) {
                            getView().renderTripRoute(routes);
                        } else {
                            getView().renderTripRouteWithoutAnimation(routes);
                        }


                        if (activeRideRequest != null) {
                            List<LatLng> latLngs = new ArrayList<LatLng>();
                            latLngs.add(new LatLng(activeRideRequest.getDestination().getLatitude(), activeRideRequest.getDestination().getLongitude()));

                            //draw vehicle location based on last update
                            if (activeRideRequest.getLocation() != null) {
                                getView().reDrawDriverMarker(activeRideRequest);
                            }

                            getView().renderSourceMarker(activeRideRequest.getPickup().getLatitude(), activeRideRequest.getPickup().getLongitude());
                            getView().renderDestinationMarker(activeRideRequest.getDestination().getLatitude(), activeRideRequest.getDestination().getLongitude());
                            if (overviewPolylines.size() > 0) {
                                latLngs.add(new LatLng(
                                        overviewPolylines.get(0).getBounds().getNortheast().getLatitude(),
                                        overviewPolylines.get(0).getBounds().getNortheast().getLongitude()
                                ));

                                latLngs.add(new LatLng(
                                        overviewPolylines.get(0).getBounds().getSouthwest().getLatitude(),
                                        overviewPolylines.get(0).getBounds().getSouthwest().getLongitude()
                                ));
                            }
                            if (animation) {
                                getView().zoomMapFitByPolyline(latLngs);
                            }
                        } else {
                            if (routes.size() > 0) {
                                List<LatLng> latLngs = new ArrayList<LatLng>();
                                List<LatLng> route = routes.get(0);
                                double startLat = route.get(0).latitude;
                                double startLng = route.get(0).longitude;
                                latLngs.add(new LatLng(startLat, startLng));
                                if (route.size() > 0) {
                                    double endLat = route.get(route.size() - 1).latitude;
                                    double endLng = route.get(route.size() - 1).longitude;
                                    getView().renderSourceMarker(startLat, startLng);
                                    getView().renderDestinationMarker(endLat, endLng);
                                    latLngs.add(new LatLng(endLat, endLng));
                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getNortheast().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getNortheast().getLongitude()
                                    ));

                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLongitude()
                                    ));
                                    if (animation) {
                                        getView().zoomMapFitByPolyline(latLngs);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private void actionSetAddressInPicker(RideRequest result) {
        getView().setAddressPickerText(result.getPickup().getAddressName(), result.getDestination().getAddressName());
    }

    private void updatePolylineIfResetedByUiLifecycle(RideRequest result) {
        if (!getView().isAlreadyRouteDrawed()) {
            getView().updateSourceCoordinate(result.getPickup().getLatitude(), result.getPickup().getLongitude());
            getView().updateDestinationCoordinate(result.getDestination().getLatitude(), result.getDestination().getLongitude());
            getOverViewPolyLine(true, true);
        }
    }

    @Override
    public void getOverViewPolyLine(final boolean animate, final boolean zoomToFit) {
        double driverLat = 0;
        double driverLong = 0;
        if (activeRideRequest != null && activeRideRequest.getLocation() != null && activeRideRequest.getStatus().equalsIgnoreCase(RideStatus.IN_PROGRESS)) {
            driverLat = activeRideRequest.getLocation().getLatitude();
            driverLong = activeRideRequest.getLocation().getLongitude();
        }

        RequestParams polylineRequestParams = getView().getPolyLineParam(driverLat, driverLong);

        if (polylineRequestParams != null) {
            getOverviewPolylineUseCase.execute(polylineRequestParams, new Subscriber<List<OverviewPolyline>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                }

                @Override
                public void onNext(List<OverviewPolyline> overviewPolylines) {
                    if (isViewAttached()) {
                        List<List<LatLng>> routes = new ArrayList<>();
                        for (OverviewPolyline polyline : overviewPolylines) {
                            routes.add(PolyUtil.decode(polyline.getOverviewPolyline()));
                        }

                        if (animate) {
                            getView().renderTripRoute(routes);
                        } else {
                            getView().renderTripRouteWithoutAnimation(routes);
                        }


                        if (activeRideRequest != null) {
                            List<LatLng> latLngs = new ArrayList<LatLng>();
                            latLngs.add(new LatLng(activeRideRequest.getPickup().getLatitude(), activeRideRequest.getPickup().getLongitude()));
                            latLngs.add(new LatLng(activeRideRequest.getDestination().getLatitude(), activeRideRequest.getDestination().getLongitude()));

                            //draw vehicle location based on last update
                            if (activeRideRequest.getLocation() != null) {
                                getView().reDrawDriverMarker(activeRideRequest);
                            }

                            getView().renderSourceMarker(activeRideRequest.getPickup().getLatitude(), activeRideRequest.getPickup().getLongitude());
                            getView().renderDestinationMarker(activeRideRequest.getDestination().getLatitude(), activeRideRequest.getDestination().getLongitude());

                            if (zoomToFit) {
                                if (overviewPolylines.size() > 0) {
                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getNortheast().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getNortheast().getLongitude()
                                    ));

                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLongitude()
                                    ));
                                    getView().zoomMapFitByPolyline(
                                            latLngs
                                    );
                                }
                            }
                        } else {
                            if (routes.size() > 0) {
                                List<LatLng> latLngs = new ArrayList<LatLng>();
                                List<LatLng> route = routes.get(0);
                                double startLat = route.get(0).latitude;
                                double startLng = route.get(0).longitude;
                                latLngs.add(new LatLng(startLat, startLng));
                                if (route.size() > 0) {
                                    double endLat = route.get(route.size() - 1).latitude;
                                    double endLng = route.get(route.size() - 1).longitude;
                                    getView().renderSourceMarker(startLat, startLng);
                                    getView().renderDestinationMarker(endLat, endLng);
                                    latLngs.add(new LatLng(endLat, endLng));
                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getNortheast().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getNortheast().getLongitude()
                                    ));

                                    latLngs.add(new LatLng(
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLatitude(),
                                            overviewPolylines.get(0).getBounds().getSouthwest().getLongitude()
                                    ));
                                    if (zoomToFit) {
                                        getView().zoomMapFitByPolyline(latLngs);
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void actionShareEta() {
        getView().showShareEtaProgress();

        getRideRequestMapUseCase.execute(getView().getShareEtaParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideShareEtaProgress();
                    getView().showFailedShare();
                }
            }

            @Override
            public void onNext(String shareUrl) {
                if (isViewAttached()) {
                    getView().hideShareEtaProgress();
                    getView().showShareDialog(shareUrl);
                }
            }
        });
    }

    @Override
    public void startGetRequestDetailsPeriodicService(String requestId) {
        subscription.add(Observable.defer(new Func0<Observable<RideRequest>>() {
                    @Override
                    public Observable<RideRequest> call() {
                        if (isViewAttached()) {

                            RequestParams requestParams = getView().getCurrentRequestParams(getView().getRequestId());
                            if (currentLocation != null) {
                                requestParams.putObject(GetRideRequestDetailUseCase.PARAM_APP_LATITUDE, currentLocation.getLatitude());
                                requestParams.putObject(GetRideRequestDetailUseCase.PARAM_APP_LONGITUDE, currentLocation.getLongitude());
                            }
                            return getRideRequestUseCase.createObservable(requestParams);
                        }
                        return Observable.error(null);
                    }
                }).repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.concatMap(new Func1<Void, Observable<?>>() {
                            @Override
                            public Observable<?> call(Void aVoid) {
                                return Observable.timer(CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY, TimeUnit.MILLISECONDS);
                            }
                        });
                    }
                })
                        .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                            @Override
                            public Observable<?> call(Observable<? extends Throwable> observable) {
                                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                                    @Override
                                    public Observable<?> call(Throwable throwable) {
                                        return Observable.timer(CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY, TimeUnit.MILLISECONDS);
                                    }
                                });
                            }
                        })
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<RideRequest>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(RideRequest rideRequest) {
                                handlePeriodicServiceSuccessResponse(rideRequest);
                            }
                        })
        );

    }

    private void handlePeriodicServiceSuccessResponse(RideRequest rideRequest) {
        if (isViewAttached()) {
            if (activeRideRequest == null) {
                if (!TextUtils.isEmpty(getView().getActiveProductNameInCache())) {
                    if (getView()
                            .getActiveProductNameInCache()
                            .equalsIgnoreCase(getView().getActivity().getString(R.string.uber_moto_display_name))
                            ) {
                        getView().setDriverIcon(rideRequest, R.drawable.moto_map_icon);
                    } else {
                        getView().setDriverIcon(rideRequest, R.drawable.car_map_icon);
                    }
                }
                actionGetProductDetailToDecideIcon(rideRequest);
            }

            getView().onSuccessCreateRideRequest(rideRequest);
            activeRideRequest = rideRequest;
            proccessGetCurrentRideRequest(rideRequest);

            //update poll wait
            if (activeRideRequest != null && activeRideRequest.getPollWait() > 0) {
                CommonUtils.dumper("Latest poll wait = " + activeRideRequest.getPollWait());
                CURRENT_REQUEST_DETAIL_POLLING_TIME_DELAY = activeRideRequest.getPollWait() * 1000;
            }
        }
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
                            actionGetProductDetailToDecideIcon(rideRequest);
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
            getOverViewPolyLine(true, true);
        }
    }

    @Override
    public void actionCallDriver() {
        getView().checkAndExecuteCallPermission(activeRideRequest.getDriver().getPhoneNumber());
    }

    @Override
    public void actionMessageDriver() {
        getView().openSmsIntent(activeRideRequest.getDriver().getPhoneNumber());
    }

    @Override
    public boolean checkIsAnyPendingRequest() {
        return activeRideRequest != null;
    }

    @Override
    public void actionGoToCurrentLocation() {
        if (currentLocation != null) {
            getView().moveMapToLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getView().getActivity());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result))
                getView().showMessage(getView().getActivity().getString(R.string.unavailable_play_service));
            else {
                getView().showMessage(getView().getActivity().getString(R.string.unsupported_device));
            }
            return false;
        }
        return true;
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void initializeLocationService() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getView().getActivity())
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            if (getFuzedLocation() != null) {
                                currentLocation = getFuzedLocation();
                                startLocationUpdates();
                            } else {
                                checkLocationSettings();
                            }
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        }
                    })
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        googleApiClient.connect();
    }

    /**
     * This functions checks if locations is not enabledm shows a dialog to enable to location
     */
    private void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient,
                        builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                //final LocationSettingsStates s= result.getLocationSettingsStates();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        currentLocation = getFuzedLocation();
                        startLocationUpdates();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getView().getActivity(), RideHomeMapFragment.REQUEST_CHECK_LOCATION_SETTING_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.

                        getView().showMessage(getView().getActivity().getString(R.string.msg_enter_location));
                        break;
                }
            }
        });
    }

    private Location getFuzedLocation() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return null;
        }

        return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private void startLocationUpdates() {
        if ((ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(getView().getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation = location;
            }
        });
    }


    @Override
    public void onResume() {
//        getOverViewPolyLine(true, true);
        subscription = RxUtils.getNewCompositeSubIfUnsubscribed(subscription);
        if (getView().getRequestId() != null) {
            startGetRequestDetailsPeriodicService(getView().getRequestId());
        }
    }

    @Override
    public void onPause() {
        RxUtils.unsubscribeIfNotNull(subscription);
    }

    @Override
    public void actionYesCancelBtnClicked() {
        if (activeRideRequest != null && activeRideRequest.getStatus().equalsIgnoreCase(RideStatus.IN_PROGRESS)) {
            getView().showMessage(getView().getActivity().getString(R.string.error_cannot_cancel));
            return;
        }

        if (!TextUtils.isEmpty(getView().getRequestId())) {
            if (activeRideRequest != null) {
                if (activeRideRequest.getCancelChargeTimestamp() != null)
                    getView().actionNavigateToCancelReasonPage(getView().getRequestId(), activeRideRequest.getCancelChargeTimestamp());
                else {
                    getView().actionNavigateToCancelReasonPage(getView().getRequestId());
                }
            }
        }
    }

    @Override
    public String getRideStatus() {
        if (activeRideRequest == null) {
            return null;
        }
        return activeRideRequest.getStatus();
    }

    @Override
    public void actionCancelButtonClicked() {
        if (activeRideRequest != null) {
            if (activeRideRequest.getCancelChargeTimestamp() == null) return;
            SimpleDateFormat serverDateFormatter = new SimpleDateFormat(DATE_SERVER_FORMAT, Locale.US);
            Date date = null;
            try {
                date = serverDateFormatter.parse(activeRideRequest.getCancelChargeTimestamp());
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }
            Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));

            long delta = date.getTime() - now.getTimeInMillis();
            new CountDownTimer(delta, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (isViewAttached())
                        getView().showCancellationLayout();
                }
            }.start();
        }
    }

    @Override
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
