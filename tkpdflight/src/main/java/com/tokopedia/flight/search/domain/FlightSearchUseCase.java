package com.tokopedia.flight.search.domain;

import android.text.TextUtils;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.data.db.model.FlightSearchSingleRouteDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
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
                final List<String> searchResDistinctAirlineIds = new ArrayList<>();
                final List<String> searchResDistinctAirportIds = new ArrayList<>();

                // convert from List of DBModel to List of ViewModel
                final List<FlightSearchViewModel> flightSearchViewModelList = new ArrayList<>();
                for (int i = 0, sizei = flightSearchSingleRouteDBs.size(); i < sizei; i++) {
                    flightSearchViewModelList.add(new FlightSearchViewModel(flightSearchSingleRouteDBs.get(i)));
                }

                // select distinct all airline and airports in routes
                for (int i = 0, sizei = flightSearchViewModelList.size(); i < sizei; i++) {
                    FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
                    List<Route> routeList = flightSearchViewModel.getRouteList();
                    for (int j = 0, sizej = routeList.size(); i < sizej; i++) {
                        Route route = routeList.get(j);
                        String airline = route.getAirline();
                        String departureAirport = route.getDepartureAirport();
                        String arrivalAirport = route.getArrivalAirport();

                        if (!TextUtils.isEmpty(airline) && !searchResDistinctAirlineIds.contains(airline)) {
                            searchResDistinctAirlineIds.add(airline);
                        }
                        if (!TextUtils.isEmpty(departureAirport) && !searchResDistinctAirportIds.contains(departureAirport)) {
                            searchResDistinctAirportIds.add(departureAirport);
                        }
                        if (!TextUtils.isEmpty(arrivalAirport) && !searchResDistinctAirportIds.contains(arrivalAirport)) {
                            searchResDistinctAirportIds.add(arrivalAirport);
                        }
                    }
                }

                //get airlines info to merge with the view model
                return Observable.zip(
                        flightRepository.getAirportList(searchResDistinctAirportIds),
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
                                                       List<FlightAirlineDB> airlineDBList){
        HashMap<String, FlightAirlineDB> dbAirlineMaps = new HashMap<>();
        HashMap<String, FlightAirportDB> dbAirportMaps = new HashMap<>();
        for (int i = 0, sizei = airlineDBList.size(); i < sizei; i++) {
            dbAirlineMaps.put(airlineDBList.get(i).getId(), airlineDBList.get(i));
        }
        for (int i = 0, sizei = airportDBList.size(); i < sizei; i++) {
            dbAirportMaps.put(airportDBList.get(i).getAirportId(), airportDBList.get(i));
        }
        for (int i = 0, sizei = flightSearchViewModelList.size(); i<sizei; i++) {
            FlightSearchViewModel flightSearchViewModel = flightSearchViewModelList.get(i);
            List<Route> routeList = flightSearchViewModel.getRouteList();
            List<FlightAirlineDB> airlineDBArrayList = new ArrayList<>();
            for (int j = 0, sizej = routeList.size(); j < sizej; j++) {
                Route route = routeList.get(j);
                String airlineID = route.getAirline();
                if (dbAirlineMaps.containsKey(airlineID)){
                    String airlineNameFromMap = dbAirlineMaps.get(airlineID).getName();
                    String airlineLogoFromMap = dbAirlineMaps.get(airlineID).getLogo();
                    route.setAirlineName(airlineNameFromMap);
                    route.setAirlineLogo(airlineLogoFromMap);
                    airlineDBArrayList.add(new FlightAirlineDB(airlineID, airlineNameFromMap, airlineLogoFromMap));
                } else {
                    airlineDBArrayList.add(new FlightAirlineDB(airlineID, "",""));
                }

                String depAirportID = route.getDepartureAirport();
                if (dbAirportMaps.containsKey(depAirportID)){
                    String name = dbAirportMaps.get(depAirportID).getAirportName();
                    String city = dbAirportMaps.get(depAirportID).getCityName();
                    route.setDepartureAirportName(name);
                    route.setDepartureAirportCity(city);
                }
                String arrAirportID = route.getArrivalAirport();
                if (dbAirportMaps.containsKey(arrAirportID)){
                    String name = dbAirportMaps.get(arrAirportID).getAirportName();
                    String city = dbAirportMaps.get(arrAirportID).getCityName();
                    route.setArrivalAirportName(name);
                    route.setArrivalAirportCity(city);
                }
            }
            flightSearchViewModel.setAirlineDataList(airlineDBArrayList);

            String depAirport = flightSearchViewModel.getDepartureAirport();
            if (dbAirportMaps.containsKey(depAirport)){
                String name = dbAirportMaps.get(depAirport).getAirportName();
                String city = dbAirportMaps.get(depAirport).getCityName();
                flightSearchViewModel.setDepartureAirportName(name);
                flightSearchViewModel.setDepartureAirportCity(city);
            }

            String arrAirport = flightSearchViewModel.getArrivalAirport();
            if (dbAirportMaps.containsKey(arrAirport)){
                String name = dbAirportMaps.get(arrAirport).getAirportName();
                String city = dbAirportMaps.get(arrAirport).getCityName();
                flightSearchViewModel.setArrivalAirportName(name);
                flightSearchViewModel.setArrivalAirportCity(city);
            }
        }
        return flightSearchViewModelList;
    }

    public static RequestParams generateRequestParams(FlightSearchPassDataViewModel flightSearchPassDataViewModel,
                                                      boolean isReturning, boolean fromCache, FlightFilterModel flightFilterModel,
                                                      @FlightSortOption int sortOption) {
        return FlightSearchParamUtil.generateRequestParams(flightSearchPassDataViewModel,
                isReturning, fromCache, flightFilterModel, sortOption);
    }

}
