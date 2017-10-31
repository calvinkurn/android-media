package com.tokopedia.flight.airline.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airline.domain.FlightAirlineUseCase;
import com.tokopedia.flight.airline.view.FlightAirlineView;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirlinePresenter extends BaseDaggerPresenter<FlightAirlineView> {

    private final FlightAirlineUseCase flightAirlineUseCase;
    private Subscriber<List<FlightAirlineDB>> subscriberAirline;

    @Inject
    public FlightAirlinePresenter(FlightAirlineUseCase flightAirlineUseCase) {
        this.flightAirlineUseCase = flightAirlineUseCase;
    }

    public void getAirlines() {
        getAirlines(null);
    }

    public void getAirlines(String airlineID) {
        flightAirlineUseCase.execute(FlightAirlineUseCase.createRequestParams(airlineID),
                getSubscriberAirline());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightAirlineUseCase.unsubscribe();
    }

    private Subscriber<List<FlightAirlineDB>> getSubscriberAirline() {
        return new Subscriber<List<FlightAirlineDB>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(List<FlightAirlineDB> flightAirlineDBs) {
                getView().onSearchLoaded(flightAirlineDBs,flightAirlineDBs.size());
            }
        };
    }

}
