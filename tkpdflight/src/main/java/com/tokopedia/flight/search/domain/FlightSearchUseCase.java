package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * @author by zulfikarrahman on 10/25/17.
 */

public class FlightSearchUseCase extends UseCase<List<FlightSearchViewModel>> {
    private final FlightRepository flightRepository;

    @Inject
    public FlightSearchUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public static RequestParams generateRequestParams(FlightSearchApiRequestModel flightSearchApiRequestModel,
                                                      boolean isReturning,
                                                      boolean fromCache,
                                                      FlightFilterModel flightFilterModel,
                                                      @FlightSortOption int sortOption) {
        return FlightSearchParamUtil.generateRequestParams(
                flightSearchApiRequestModel,
                isReturning,
                fromCache,
                flightFilterModel,
                sortOption
        );
    }

    @Override
    public Observable<List<FlightSearchViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getFlightSearch(requestParams).flatMap(new Func1<List<FlightSearchSingleRouteDB>, Observable<List<FlightSearchViewModel>>>() {
            @Override
            public Observable<List<FlightSearchViewModel>> call(List<FlightSearchSingleRouteDB> flightSearchSingleRouteDBs) {
                if (flightSearchSingleRouteDBs == null) {
                    return Observable.just((List<FlightSearchViewModel>) new ArrayList<FlightSearchViewModel>());
                }
                List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i < sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }

                return Observable.from(flightSearchViewModelList)
                        .flatMap(new Func1<FlightSearchViewModel, Observable<FlightSearchViewModel>>() {
                            @Override
                            public Observable<FlightSearchViewModel> call(FlightSearchViewModel flightSearchViewModel) {
                                return Observable.zip(
                                        Observable.from(flightSearchViewModel.getRouteList())
                                                .flatMap(new Func1<Route, Observable<Route>>() {
                                                    @Override
                                                    public Observable<Route> call(Route route) {
                                                        return Observable.zip(
                                                                flightRepository.getAirportById(route.getDepartureAirport()),
                                                                flightRepository.getAirportById(route.getArrivalAirport()),
                                                                Observable.just(route),
                                                                new Func3<FlightAirportDB, FlightAirportDB, Route, Route>() {
                                                                    @Override
                                                                    public Route call(FlightAirportDB departureAirport,
                                                                                      FlightAirportDB arrivalAirport,
                                                                                      Route route) {
                                                                        if (departureAirport != null) {
                                                                            route.setDepartureAirportCity(departureAirport.getCityName());
                                                                            route.setDepartureAirportName(departureAirport.getAirportName());
                                                                        }
                                                                        if (arrivalAirport != null) {
                                                                            route.setArrivalAirportCity(arrivalAirport.getCityName());
                                                                            route.setArrivalAirportName(arrivalAirport.getAirportName());
                                                                        }
                                                                        return route;
                                                                    }
                                                                }
                                                        );
                                                    }
                                                }).toList(),
                                        Observable.just(flightSearchViewModel).zipWith(flightRepository.getAirportById(flightSearchViewModel.getDepartureAirport()), new Func2<FlightSearchViewModel, FlightAirportDB, FlightSearchViewModel>() {
                                            @Override
                                            public FlightSearchViewModel call(FlightSearchViewModel flightSearchViewModel, FlightAirportDB airportDB) {
                                                if (airportDB != null) {
                                                    flightSearchViewModel.setDepartureAirportCity(airportDB.getCityName());
                                                    flightSearchViewModel.setDepartureAirportName(airportDB.getAirportName());
                                                }
                                                return flightSearchViewModel;
                                            }
                                        }).zipWith(flightRepository.getAirportById(flightSearchViewModel.getArrivalAirport()), new Func2<FlightSearchViewModel, FlightAirportDB, FlightSearchViewModel>() {
                                            @Override
                                            public FlightSearchViewModel call(FlightSearchViewModel flightSearchViewModel, FlightAirportDB airportDB) {
                                                if (airportDB != null) {
                                                    flightSearchViewModel.setArrivalAirportCity(airportDB.getCityName());
                                                    flightSearchViewModel.setArrivalAirportName(airportDB.getAirportName());
                                                }
                                                return flightSearchViewModel;
                                            }
                                        }),
                                        new Func2<List<Route>, FlightSearchViewModel, FlightSearchViewModel>() {
                                            @Override
                                            public FlightSearchViewModel call(List<Route> routes, FlightSearchViewModel flightSearchViewModel) {
                                                flightSearchViewModel.setRouteList(routes);
                                                return flightSearchViewModel;
                                            }
                                        }

                                );
                            }
                        })
                        .flatMap(new Func1<FlightSearchViewModel, Observable<FlightSearchViewModel>>() {
                            @Override
                            public Observable<FlightSearchViewModel> call(FlightSearchViewModel flightSearchViewModel) {
                                List<String> airlineList = new ArrayList<>();
                                for (Route route : flightSearchViewModel.getRouteList()) {
                                    airlineList.add(route.getAirline());
                                }
                                return Observable.zip(
                                        Observable.from(airlineList)
                                                .flatMap(new Func1<String, Observable<FlightAirlineDB>>() {
                                                    @Override
                                                    public Observable<FlightAirlineDB> call(String airlineId) {
                                                        return Observable.zip(flightRepository.getAirlineById(airlineId),
                                                                Observable.just(airlineId), new Func2<FlightAirlineDB, String, FlightAirlineDB>() {
                                                                    @Override
                                                                    public FlightAirlineDB call(FlightAirlineDB flightAirlineDB, String flightAirlineDB2) {
                                                                        return flightAirlineDB;
                                                                    }
                                                                });
                                                    }
                                                }).toList(),
                                        Observable.just(flightSearchViewModel),
                                        new Func2<List<FlightAirlineDB>, FlightSearchViewModel, FlightSearchViewModel>() {
                                            @Override
                                            public FlightSearchViewModel call(List<FlightAirlineDB> flightAirlineDBS, FlightSearchViewModel flightSearchViewModel) {
                                                flightSearchViewModel.setAirlineDataList(flightAirlineDBS);
                                                for (Route route : flightSearchViewModel.getRouteList()) {
                                                    for (FlightAirlineDB flightAirlineDB : flightAirlineDBS) {
                                                        if (route.getAirline().equalsIgnoreCase(flightAirlineDB.getId())) {
                                                            route.setAirlineLogo(flightAirlineDB.getLogo());
                                                            route.setAirlineName(flightAirlineDB.getName());
                                                        }
                                                    }
                                                }
                                                return flightSearchViewModel;
                                            }
                                        }
                                );
                            }
                        })
                        .toList();

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

}
