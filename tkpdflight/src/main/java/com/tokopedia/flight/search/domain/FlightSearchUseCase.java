package com.tokopedia.flight.search.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightSearchUseCase extends UseCase<List<FlightSearchViewModel>> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightSearchUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightSearchViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightSearch(requestParams).flatMap(new Func1<List<FlightSearchSingleRouteDB>, Observable<List<FlightSearchViewModel>>>() {
            @Override
            public Observable<List<FlightSearchViewModel>> call(List<FlightSearchSingleRouteDB> flightSearchSingleRouteDBs) {
                if (flightSearchSingleRouteDBs == null) {
                    return Observable.just((List<FlightSearchViewModel>)new ArrayList<FlightSearchViewModel>());
                }
                final List<String> searchResDistinctAirlineIds = new ArrayList<>();

                // convert from List of DBModel to List of ViewModel
                final List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i < sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }

                // select distinct all airline and airports in routes
                for (int i = 0, sizei = flightSearchViewModelList.size(); i < sizei; i++) {
                    FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
                    List<Route> routeList = flightSearchViewModel.getRouteList();
                    for (int j = 0, sizej = routeList.size(); j < sizej; j++) {
                        Route route = routeList.get(j);
                        String airline = route.getAirline();
                        if (!TextUtils.isEmpty(airline) && !searchResDistinctAirlineIds.contains(airline)) {
                            searchResDistinctAirlineIds.add(airline);
                        }
                    }
                }

                //get airlines info *from cache first* to merge with the view model
                return Observable.zip(
                        flightRepository.getAirportList(""),
                        flightRepository.getAirlineList(searchResDistinctAirlineIds),
                        new Func2<List<FlightAirportDB>, List<FlightAirlineDB>, List<FlightSearchViewModel>>() {
                            @Override
                            public List<FlightSearchViewModel> call(List<FlightAirportDB> flightAirportDBs, List<FlightAirlineDB> flightAirlineDBs) {
                                return mergeViewModel(flightSearchViewModelList, flightAirportDBs, flightAirlineDBs);
                            }
                        });
            }
        });
    }

    private List<FlightSearchViewModel> mergeViewModel(List<FlightSearchViewModel> flightSearchViewModelList,
                                                       List<FlightAirportDB> airportDBList,
                                                       List<FlightAirlineDB> airlineDBList) {
        HashMap<String, FlightAirlineDB> dbAirlineMaps = new HashMap<>();
        HashMap<String, FlightAirportDB> dbAirportMaps = new HashMap<>();
        for (int i = 0, sizei = airlineDBList.size(); i < sizei; i++) {
            dbAirlineMaps.put(airlineDBList.get(i).getId(), airlineDBList.get(i));
        }
        for (int i = 0, sizei = airportDBList.size(); i < sizei; i++) {
            dbAirportMaps.put(airportDBList.get(i).getAirportId(), airportDBList.get(i));
        }
        for (int i = 0, sizei = flightSearchViewModelList.size(); i < sizei; i++) {
            FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
            flightSearchViewModel.mergeWithAirportAndAirlines(dbAirlineMaps, dbAirportMaps);
        }
        return flightSearchViewModelList;
    }

    public static RequestParams generateRequestParams(FlightSearchApiRequestModel flightSearchApiRequestModel,
                                                      boolean isReturning, boolean fromCache, FlightFilterModel flightFilterModel,
                                                      @FlightSortOption int sortOption) {
        return FlightSearchParamUtil.generateRequestParams(flightSearchApiRequestModel,
                isReturning, fromCache, flightFilterModel, sortOption);
    }

}
