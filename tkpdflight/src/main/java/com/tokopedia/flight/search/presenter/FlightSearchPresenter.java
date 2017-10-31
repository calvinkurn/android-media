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
    private final FlightAirlineUseCase flightAirlineUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchUseCase flightSearchUseCase, FlightAirlineUseCase flightAirlineUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
        this.flightAirlineUseCase = flightAirlineUseCase;
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
                List<String>airlineIds = new ArrayList<>();

                List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i<sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }

                // select distinct all airline in routes, then compare with DB
                // if any airline is not found, then retrieve all airlines from cloud
                for (int i = 0, sizei = flightSearchViewModelList.size(); i<sizei; i++) {
                    FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
                    List<Route> routeList = flightSearchViewModel.getRouteList();
                    for (int j = 0, sizej = routeList.size(); i<sizej; i++) {
                        String airline = routeList.get(j).getAirline();
                        if (TextUtils.isEmpty(airline)) {
                            continue;
                        }
                        if (!airlineIds.contains(airline)) {
                            airlineIds.add(airline);
                        }
                    }
                }


                getView().onSearchLoaded(flightSearchViewModelList,flightSearchSingleRouteDBs.size());
            }
        };
    }
}
