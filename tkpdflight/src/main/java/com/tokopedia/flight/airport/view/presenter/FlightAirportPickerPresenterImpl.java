package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportVersionCheckUseCase;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;

import java.util.ArrayList;
import java.util.List;

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

    @Inject
    public FlightAirportPickerPresenterImpl(FlightAirportPickerUseCase flightAirportPickerUseCase) {
        this.flightAirportPickerUseCase = flightAirportPickerUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getAirportList(String text, boolean isFirstTime) {
        getView().showGetAirportListLoading();
        compositeSubscription.add(flightAirportPickerUseCase
                .createObservable(FlightAirportPickerUseCase.createRequestParams(text))
                .map(new Func1<List<FlightAirportDB>, List<FlightCountryAirportViewModel>>() {
                    @Override
                    public List<FlightCountryAirportViewModel> call(List<FlightAirportDB> airports) {
                        List<FlightCountryAirportViewModel> countries = new ArrayList<>();
                        if (airports != null && airports.size() > 0) {
                            FlightCountryAirportViewModel country = new FlightCountryAirportViewModel();
                            country.setCountryId(airports.get(0).getCountryId());
                            country.setCountryName(airports.get(0).getCountryName());
                            List<FlightAirportViewModel> airportViewModels = new ArrayList<>();
                            for (int i = 0; i < airports.size(); i++) {
                                FlightAirportDB airport = airports.get(i);
                                FlightAirportViewModel airportViewModel = new FlightAirportViewModel();
                                airportViewModel.setAirportName(airport.getAirportName());
                                airportViewModel.setCountryName(country.getCountryName());
                                if (airport.getAirportId() != null && airport.getAirportId().length() > 0) {
                                    airportViewModel.setAirportCode(airport.getAirportId());
                                } else {
                                    airportViewModel.setCityAirports(airport.getAirportIds().split(","));
                                }
                                airportViewModel.setCityCode(airport.getCityCode());
                                airportViewModel.setCityName(airport.getCityName());
                                if (!country.getCountryName().equalsIgnoreCase(airport.getCountryName())) {
                                    if (airportViewModels.size() > 0) {
                                        country.setAirports(airportViewModels);
                                        if (country.getCountryId().equalsIgnoreCase("ID") && countries.size() > 0) {
                                            countries.add(0, country);
                                        } else {
                                            countries.add(country);
                                        }
                                    }
                                    country = new FlightCountryAirportViewModel();
                                    country.setCountryId(airport.getCountryId());
                                    country.setCountryName(airport.getCountryName());
                                    airportViewModels = new ArrayList<>();
                                }

                                airportViewModels.add(airportViewModel);
                                if (i == airports.size() - 1) {
                                    country.setAirports(airportViewModels);
                                    if (country.getCountryId().equalsIgnoreCase("ID") && countries.size() > 0) {
                                        countries.add(0, country);
                                    } else {
                                        countries.add(country);
                                    }
                                }
                            }
                        }

                        return countries;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getSubscriberGetAirportList())
        );
    }

    private Subscriber<List<FlightCountryAirportViewModel>> getSubscriberGetAirportList() {
        return new Subscriber<List<FlightCountryAirportViewModel>>() {
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
            public void onNext(List<FlightCountryAirportViewModel> flightAirportDBs) {
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
}
