package com.tokopedia.ride.history.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;
import com.tokopedia.ride.history.domain.model.RideHistory;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryDetailPresenter extends BaseDaggerPresenter<RideHistoryDetailContract.View> implements RideHistoryDetailContract.Presenter {
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetSingleRideHistoryUseCase getSingleRideHistoryUseCase;

    public RideHistoryDetailPresenter(GetSingleRideHistoryUseCase getSingleRideHistoryUseCase, GetOverviewPolylineUseCase getOverviewPolylineUseCase) {
        this.getSingleRideHistoryUseCase = getSingleRideHistoryUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
    }

    @Override
    public void initialize() {
        getView().showProgressBar();
        getView().hideMainLayout();
        actionGetSingleHistory();
    }

    private void actionGetSingleHistory() {
        getView().showProgressBar();
        getSingleRideHistoryUseCase.execute(getView().getSingleHistoryParam(), new Subscriber<RideHistory>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideProgressBar();
                    getView().showErrorLayout();
                }
            }

            @Override
            public void onNext(RideHistory rideHistory) {
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideProgressBar();
                    getView().renderHistory(rideHistory);
                    actionGetRenderedLocation(rideHistory);
                }
            }
        });
    }

    private void actionGetRenderedLocation(final RideHistory rideHistory) {
        if (rideHistory.getPickup() != null) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
                                rideHistory.getPickup().getLatitude(),
                                rideHistory.getPickup().getLongitude()
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onNext(String.valueOf(rideHistory.getPickup().getLatitude()) + ", " + String.valueOf(rideHistory.getPickup().getLongitude()));
                    }
                }
            }).onErrorReturn(new Func1<Throwable, String>() {
                @Override
                public String call(Throwable throwable) {
                    return String.valueOf(String.valueOf(rideHistory.getPickup().getLatitude()) + ", " + String.valueOf(rideHistory.getPickup().getLongitude()));
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(String sourceAddress) {
                            if (isViewAttached()) {
                                getView().setPickupLocationText(sourceAddress);
                            }
                        }
                    });
        }

        if (rideHistory.getDestination() != null) {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    try {
                        subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
                                rideHistory.getPickup().getLatitude(),
                                rideHistory.getPickup().getLongitude()
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                        subscriber.onNext(String.valueOf(rideHistory.getPickup().getLatitude()) + ", " + String.valueOf(rideHistory.getPickup().getLongitude()));
                    }
                }
            }).onErrorReturn(new Func1<Throwable, String>() {
                @Override
                public String call(Throwable throwable) {
                    return String.valueOf(String.valueOf(rideHistory.getPickup().getLatitude()) + ", " + String.valueOf(rideHistory.getPickup().getLongitude()));
                }
            })
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(String sourceAddress) {
                            if (isViewAttached()) {
                                getView().setDestinationLocation(sourceAddress);
                            }
                        }
                    });
        }
    }
}
