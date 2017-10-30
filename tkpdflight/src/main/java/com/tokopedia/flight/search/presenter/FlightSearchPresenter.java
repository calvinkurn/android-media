package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
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
                List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i<sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }
                getView().onSearchLoaded(flightSearchViewModelList,flightSearchSingleRouteDBs.size());
            }
        };
    }
}
