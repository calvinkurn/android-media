package com.tokopedia.flight.search.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

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
                final List<String> searchResAirlineIds = new ArrayList<>();

                // convert from List of DBModel to List of ViewModel
                final List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i < sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }

                // select distinct all airline in routes, then compare with DB
                // if any airline is not found, then retrieve all airlines from cloud
                for (int i = 0, sizei = flightSearchViewModelList.size(); i < sizei; i++) {
                    FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
                    List<Route> routeList = flightSearchViewModel.getRouteList();
                    for (int j = 0, sizej = routeList.size(); i < sizej; i++) {
                        String airline = routeList.get(j).getAirline();
                        if (TextUtils.isEmpty(airline)) {
                            continue;
                        }
                        if (!searchResAirlineIds.contains(airline)) {
                            searchResAirlineIds.add(airline);
                        }
                    }
                }

                //get airlines info to merge with the view model
                return flightRepository.getAirlineList().flatMap(new Func1<List<FlightAirlineDB>, Observable<List<FlightSearchViewModel>>>() {
                    @Override
                    public Observable<List<FlightSearchViewModel>> call(List<FlightAirlineDB> flightAirlineDBs) {
                        return mergeAirLineAndViewModel(flightAirlineDBs, searchResAirlineIds, flightSearchViewModelList).flatMap(new Func1<FlightSearchMerge, Observable<List<FlightSearchViewModel>>>() {
                            @Override
                            public Observable<List<FlightSearchViewModel>> call(FlightSearchMerge flightSearchMerge) {
                                if (flightSearchMerge.isAirlineInCache()) {
                                    return Observable.just(flightSearchMerge.getFlightSearchViewModelList());
                                } else {
                                    // if the airline is not in db, retry get from cloud.
                                    return getCloudListObservable(searchResAirlineIds, flightSearchViewModelList);
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private Observable<List<FlightSearchViewModel>> getCloudListObservable(final List<String> searchResAirlineIds,
                                                                           final List<FlightSearchViewModel> flightSearchViewModelList) {
        return flightRepository.makeAirlineExpired().flatMap(new Func1<Boolean, Observable<List<FlightSearchViewModel>>>() {
            @Override
            public Observable<List<FlightSearchViewModel>> call(Boolean aBoolean) {
                return flightRepository.getAirlineList().flatMap(new Func1<List<FlightAirlineDB>, Observable<List<FlightSearchViewModel>>>() {
                    @Override
                    public Observable<List<FlightSearchViewModel>> call(List<FlightAirlineDB> flightAirlineDBs) {
                        return mergeAirLineAndViewModel(flightAirlineDBs, searchResAirlineIds, flightSearchViewModelList).flatMap(new Func1<FlightSearchMerge, Observable<List<FlightSearchViewModel>>>() {
                            @Override
                            public Observable<List<FlightSearchViewModel>> call(FlightSearchMerge flightSearchMerge) {
                                return Observable.just(flightSearchMerge.getFlightSearchViewModelList());
                            }
                        });
                    }
                });
            }
        });
    }

    private class FlightSearchMerge{
        List<FlightSearchViewModel> flightSearchViewModelList;
        boolean isAirlineInCache;

        private FlightSearchMerge(List<FlightSearchViewModel> flightSearchViewModelList, boolean isAirlineInCache) {
            this.flightSearchViewModelList = flightSearchViewModelList;
            this.isAirlineInCache = isAirlineInCache;
        }

        private List<FlightSearchViewModel> getFlightSearchViewModelList() {
            return flightSearchViewModelList;
        }

        private boolean isAirlineInCache() {
            return isAirlineInCache;
        }
    }

    private Observable<FlightSearchMerge> mergeAirLineAndViewModel(List<FlightAirlineDB> flightAirlineDBs,
                                                                             List<String> searchResAirlineIds,
                                                                             List<FlightSearchViewModel> flightSearchViewModelList){
        boolean isAirlineInCache = true;

        HashMap<String, FlightAirlineDB> dbAirlineMaps = new HashMap<>();
        for (int i = 0, sizei = flightAirlineDBs.size(); i < sizei; i++) {
            dbAirlineMaps.put(flightAirlineDBs.get(i).getId(), flightAirlineDBs.get(i));
        }
        for (int i = 0, sizei = searchResAirlineIds.size(); i < sizei; i++) {
            if (! dbAirlineMaps.containsKey(searchResAirlineIds.get(i))) {
                isAirlineInCache = false;
                break;
            }
        }
        if (isAirlineInCache) {
            for (int i = 0, sizei = flightSearchViewModelList.size(); i<sizei; i++) {
                FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
                List<Route> routeList = flightSearchViewModel.getRouteList();
                String airLineNameString = "";
                String airLineLogoString = "";
                for (int j = 0, sizej = routeList.size(); j < sizej; j++) {
                    Route route = routeList.get(j);
                    String airlineID = route.getAirline();
                    String airlineNameFromMap = dbAirlineMaps.get(airlineID).getName();
                    String airlineLogoFromMap = dbAirlineMaps.get(airlineID).getLogo();
                    route.setAirlineName(airlineNameFromMap);
                    route.setAirlineLogo(airlineLogoFromMap);

                    if (!TextUtils.isEmpty(airLineNameString)) {
                        airLineNameString+="-";
                    }
                    airLineNameString+=airlineNameFromMap;

                    if (!TextUtils.isEmpty(airLineLogoString)) {
                        airLineLogoString+="-";
                    }
                    airLineLogoString+=airlineLogoFromMap;
                }
                flightSearchViewModel.setAirlineName(airLineNameString);
                flightSearchViewModel.setAirlineLogo(airLineLogoString);
            }
            FlightSearchMerge flightSearchMerge = new FlightSearchMerge(flightSearchViewModelList, true);
            return Observable.just(flightSearchMerge);
        } else {
            return Observable.just(new FlightSearchMerge(flightSearchViewModelList, false));
        }
    }

    public static RequestParams generateRequestParams(boolean isReturning) {
        return FlightSearchParamUtil.generateRequestParams(isReturning);
    }

}
