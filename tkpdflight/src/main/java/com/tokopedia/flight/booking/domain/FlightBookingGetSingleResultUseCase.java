package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
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
import rx.functions.Func3;

/**
 * @author by alvarisi on 11/9/17.
 */

public class FlightBookingGetSingleResultUseCase extends UseCase<FlightSearchViewModel> {
    private static final String PARAM_ID = "id";
    private static final String PARAM_IS_RETURNING = "is_returning";
    private FlightRepository flightRepository;

    @Inject
    public FlightBookingGetSingleResultUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<FlightSearchViewModel> createObservable(RequestParams requestParams) {
        final String id = requestParams.getString(PARAM_ID, "");
        final boolean isReturning = requestParams.getBoolean(PARAM_IS_RETURNING, false);
        return flightRepository.getFlightSearchById(isReturning, id)
                .flatMap(new Func1<FlightSearchSingleRouteDB, Observable<FlightSearchViewModel>>() {
                    @Override
                    public Observable<FlightSearchViewModel> call(FlightSearchSingleRouteDB flightSearchSingleRouteDB) {
                        return Observable.just(new FlightSearchViewModel(flightSearchSingleRouteDB));
                    }
                })
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
                                Observable.just(flightSearchViewModel),
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

                        return Observable.zip(Observable.from(airlineList)
                                        .flatMap(new Func1<String, Observable<FlightAirlineDB>>() {
                                            @Override
                                            public Observable<FlightAirlineDB> call(String airlineId) {
                                                return Observable.zip(flightRepository.getAirlineById(airlineId), Observable.just(airlineId), new Func2<FlightAirlineDB, String, FlightAirlineDB>() {
                                                    @Override
                                                    public FlightAirlineDB call(FlightAirlineDB flightAirlineDB, String s) {
                                                        return flightAirlineDB;
                                                    }
                                                });
                                            }
                                        }).toList(), Observable.just(flightSearchViewModel),
                                new Func2<List<FlightAirlineDB>, FlightSearchViewModel, FlightSearchViewModel>() {
                                    @Override
                                    public FlightSearchViewModel call(List<FlightAirlineDB> flightAirlineDBS, FlightSearchViewModel flightSearchViewModel) {
                                        flightSearchViewModel.setAirlineDataList(flightAirlineDBS);
                                        for (Route route : flightSearchViewModel.getRouteList()) {
                                            for (FlightAirlineDB flightAirlineDB : flightAirlineDBS){
                                                if (route.getAirline().equalsIgnoreCase(flightAirlineDB.getId())){
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
                });
                        /*return Observable.zip(
                                flightRepository.getAirlineList(),
                                flightRepository.getAirportList(""),
                                Observable.just(flightSearchViewModel),
                                new Func3<List<FlightAirlineDB>, List<FlightAirportDB>,
                                        FlightSearchViewModel, FlightSearchViewModel>() {
                                    @Override
                                    public FlightSearchViewModel call(List<FlightAirlineDB> airlineDBList,
                                                                      List<FlightAirportDB> flightAirportDBs,
                                                                      FlightSearchViewModel flightSearchViewModel) {
                                        HashMap<String, FlightAirlineDB> dbAirlineMaps = new HashMap<>();
                                        HashMap<String, FlightAirportDB> dbAirportMaps = new HashMap<>();
                                        for (int i = 0, sizei = airlineDBList.size(); i < sizei; i++) {
                                            dbAirlineMaps.put(airlineDBList.get(i).getId(), airlineDBList.get(i));
                                        }
                                        for (int i = 0, sizei = flightAirportDBs.size(); i < sizei; i++) {
                                            dbAirportMaps.put(flightAirportDBs.get(i).getAirportId(), flightAirportDBs.get(i));
                                        }
                                        flightSearchViewModel.mergeWithAirportAndAirlines(dbAirlineMaps, dbAirportMaps);
                                        return flightSearchViewModel;
                                    }
                                });
                    }
                });*/
    }

    public RequestParams createRequestParam(boolean isReturning, String id) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_ID, id);
        requestParams.putBoolean(PARAM_IS_RETURNING, isReturning);
        return requestParams;
    }
}
