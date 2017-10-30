package com.tokopedia.flight.flightsearch.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerView;
import com.tokopedia.flight.flightsearch.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.flightsearch.domain.FlightSearchUseCase;
import com.tokopedia.flight.flightsearch.view.FlightSearchView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    private final FlightSearchUseCase flightSearchUseCase;
    private Subscriber<List<FlightSearchSingleRouteDB>> subscriberSearchFlight;

    @Inject
    public FlightSearchPresenter(FlightSearchUseCase flightSearchUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
    }

    //TODO params
    public void searchFlight() {
        flightSearchUseCase.execute(null, getSubscriberSearchFlight());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchUseCase.unsubscribe();
    }

    private Subscriber<List<FlightSearchSingleRouteDB>> getSubscriberSearchFlight() {
        return new Subscriber<List<FlightSearchSingleRouteDB>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(List<FlightSearchSingleRouteDB> flightSearchSingleRouteDBs) {
                getView().onSearchLoaded(flightSearchSingleRouteDBs,flightSearchSingleRouteDBs.size());
            }
        };
    }
}
