package com.tokopedia.flight.search.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airline.domain.FlightAirlineUseCase;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    private final FlightSearchUseCase flightSearchUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchUseCase flightSearchUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
    }

    //TODO params
    public void searchDepartureFlight() {
        flightSearchUseCase.execute(FlightSearchUseCase.generateRequestParams(false),
                getSubscriberSearchFlight());
    }

    public void searchReturningFlight() {
        flightSearchUseCase.execute(FlightSearchUseCase.generateRequestParams(true),
                getSubscriberSearchFlight());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchUseCase.unsubscribe();
    }

    private Subscriber<List<FlightSearchViewModel>> getSubscriberSearchFlight() {
        return new Subscriber<List<FlightSearchViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {

                getView().onSearchLoaded(flightSearchViewModels, flightSearchViewModels.size());
            }
        };
    }
}
