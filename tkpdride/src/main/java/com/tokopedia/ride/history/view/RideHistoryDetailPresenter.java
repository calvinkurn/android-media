package com.tokopedia.ride.history.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.ride.bookingride.domain.GetOverviewPolylineUseCase;
import com.tokopedia.ride.completetrip.domain.GiveDriverRatingUseCase;
import com.tokopedia.ride.history.domain.GetSingleRideHistoryUseCase;

import rx.Subscriber;

/**
 * Created by alvarisi on 4/20/17.
 */

public class RideHistoryDetailPresenter extends BaseDaggerPresenter<RideHistoryDetailContract.View> implements RideHistoryDetailContract.Presenter {
    private GetOverviewPolylineUseCase getOverviewPolylineUseCase;
    private GetSingleRideHistoryUseCase getSingleRideHistoryUseCase;
    private GiveDriverRatingUseCase giveDriverRatingUseCase;

    public RideHistoryDetailPresenter(GetSingleRideHistoryUseCase getSingleRideHistoryUseCase,
                                      GetOverviewPolylineUseCase getOverviewPolylineUseCase,
                                      GiveDriverRatingUseCase giveDriverRatingUseCase) {
        this.getSingleRideHistoryUseCase = getSingleRideHistoryUseCase;
        this.getOverviewPolylineUseCase = getOverviewPolylineUseCase;
        this.giveDriverRatingUseCase = giveDriverRatingUseCase;
    }

    @Override
    public void initialize() {
        getView().showHistoryDetailLayout();
        getView().renderHistory();
        if (!getView().isRatingAvailable()) {
            getView().showRatingLayout();
        } else {
            getView().hideRatingLayout();
        }
    }

    @Override
    public void actionSendRating() {
        getView().hideMainLayout();
        getView().showLoading();
        giveDriverRatingUseCase.execute(getView().getRatingParam(), new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().showMainLayout();
                    getView().hideLoading();
                    getView().showRatingNetworkError();
                }
            }

            @Override
            public void onNext(String s) {
                if (isViewAttached()) {
                    getView().renderSuccessfullGiveRating();
                    getView().showSuccessRatingDialog();
                    getView().hideRatingLayout();
                    getView().showMainLayout();
                    getView().hideLoading();
                }
            }
        });
    }

//    private void actionGetRenderedLocation(final RideHistoryViewModel rideHistory) {
//        if (rideHistory.getStartLatitude() != 0 && rideHistory.getStartLongitude() != 0) {
//            Observable.create(new Observable.OnSubscribe<String>() {
//                @Override
//                public void call(Subscriber<? super String> subscriber) {
//                    try {
//                        subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
//                                rideHistory.getStartLatitude(),
//                                rideHistory.getStartLongitude()
//                        ));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        subscriber.onNext(String.valueOf(rideHistory.getStartLatitude()) + ", " + String.valueOf(rideHistory.getStartLongitude()));
//                    }
//                }
//            }).onErrorReturn(new Func1<Throwable, String>() {
//                @Override
//                public String call(Throwable throwable) {
//                    return String.valueOf(String.valueOf(rideHistory.getStartLatitude()) + ", " + String.valueOf(rideHistory.getStartLongitude()));
//                }
//            })
//                    .subscribeOn(Schedulers.newThread())
//                    .unsubscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<String>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onNext(String sourceAddress) {
//                            if (isViewAttached()) {
//                                getView().setPickupLocationText(sourceAddress);
//                            }
//                        }
//                    });
//        }
//
//        if (rideHistory.getEndLatitude() != 0 && rideHistory.getEndLongitude() != 0) {
//            Observable.create(new Observable.OnSubscribe<String>() {
//                @Override
//                public void call(Subscriber<? super String> subscriber) {
//                    try {
//                        subscriber.onNext(GeoLocationUtils.reverseGeoCodeToShortAdd(getView().getActivity(),
//                                rideHistory.getEndLatitude(),
//                                rideHistory.getEndLongitude()
//                        ));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        subscriber.onNext(String.valueOf(rideHistory.getEndLatitude()) + ", " + String.valueOf(rideHistory.getEndLongitude()));
//                    }
//                }
//            }).onErrorReturn(new Func1<Throwable, String>() {
//                @Override
//                public String call(Throwable throwable) {
//                    return String.valueOf(String.valueOf(rideHistory.getEndLatitude()) + ", " + String.valueOf(rideHistory.getEndLongitude()));
//                }
//            })
//                    .subscribeOn(Schedulers.newThread())
//                    .unsubscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<String>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//
//                        @Override
//                        public void onNext(String sourceAddress) {
//                            if (isViewAttached()) {
//                                getView().setDestinationLocation(sourceAddress);
//                            }
//                        }
//                    });
//        }
//    }
}
