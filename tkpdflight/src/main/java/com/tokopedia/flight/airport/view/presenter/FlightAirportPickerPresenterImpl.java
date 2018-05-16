package com.tokopedia.flight.airport.view.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportVersionCheckUseCase;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportPickerPresenterImpl extends BaseDaggerPresenter<FlightAirportPickerView> implements FlightAirportPickerPresenter {

    private final FlightAirportPickerUseCase flightAirportPickerUseCase;
    private CompositeSubscription compositeSubscription;
    private OnQueryListener onQueryListener;

    private interface OnQueryListener {
        void onQuerySubmit(String query);
    }

    @Inject
    public FlightAirportPickerPresenterImpl(FlightAirportPickerUseCase flightAirportPickerUseCase) {
        this.flightAirportPickerUseCase = flightAirportPickerUseCase;
        this.compositeSubscription = new CompositeSubscription();
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                onQueryListener = new OnQueryListener() {
                    @Override
                    public void onQuerySubmit(String query) {
                        CommonUtils.dumper("onQuerySubmit : " + query);
                        subscriber.onNext(String.valueOf(query));
                    }
                };
            }
        }).debounce(250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AutoCompletePlaceTextChanged());
    }

    @Override
    public void getAirportList(String text, boolean isFirstTime) {
        getView().showGetAirportListLoading();
        if (onQueryListener != null) {
            onQueryListener.onQuerySubmit(text);
        }
    }

    private Subscriber<List<Visitable>> getSubscriberGetAirportList() {
        return new Subscriber<List<Visitable>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideGetAirportListLoading();
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<Visitable> flightAirportDBs) {
                getView().hideGetAirportListLoading();
                getView().renderList(flightAirportDBs);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
    }

    private class AutoCompletePlaceTextChanged extends Subscriber<String> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String keyword) {
            if (isViewAttached() && !isUnsubscribed()) {
                getView().showLoading();
                compositeSubscription.add(flightAirportPickerUseCase
                        .createObservable(FlightAirportPickerUseCase.createRequestParams(keyword))
                        .map(new Func1<List<FlightAirportDB>, List<Visitable>>() {
                            @Override
                            public List<Visitable> call(List<FlightAirportDB> airports) {
                                List<Visitable> visitables = new ArrayList<>();
                                List<Visitable> result = new ArrayList<>();
                                FlightCountryAirportViewModel negara = null;

                                for (int i = 0; i < airports.size(); i++) {
                                    FlightAirportDB airport = airports.get(i);
                                    if (negara == null || !negara.getCountryId().equalsIgnoreCase(airport.getCountryId())) {
                                        negara = new FlightCountryAirportViewModel();
                                        negara.setCountryId(airport.getCountryId());
                                        negara.setCountryName(airport.getCountryName());
                                        negara.setAirports(new ArrayList<FlightAirportViewModel>());
                                        if (negara.getCountryId().equalsIgnoreCase("ID")) {
                                            result.add(negara);
                                        } else {
                                            visitables.add(negara);
                                        }
                                    }

                                    FlightAirportViewModel airportViewModel = new FlightAirportViewModel();
                                    airportViewModel.setAirportName(airport.getAirportName());
                                    airportViewModel.setCountryName(negara.getCountryName());
                                    if (airport.getAirportId() != null && airport.getAirportId().length() > 0) {
                                        airportViewModel.setAirportCode(airport.getAirportId());
                                    } else {
                                        airportViewModel.setCityAirports(airport.getAirportIds().split(","));
                                    }
                                    airportViewModel.setCityCode(airport.getCityCode());
                                    airportViewModel.setCityName(airport.getCityName());
                                    if (negara.getCountryId().equalsIgnoreCase("ID")) {
                                        result.add(airportViewModel);
                                    } else {
                                        visitables.add(airportViewModel);
                                    }
                                }
                                result.addAll(visitables);
                                return result;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(getSubscriberGetAirportList())
                );
            }
        }
    }
}
