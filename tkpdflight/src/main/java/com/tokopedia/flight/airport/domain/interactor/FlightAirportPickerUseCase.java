package com.tokopedia.flight.airport.domain.interactor;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;
import com.tokopedia.flight.airport.view.viewmodel.FlightCountryAirportViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 10/25/17.
 */

public class FlightAirportPickerUseCase extends UseCase<List<Visitable>> {
    private static final String ID_COUNTRY_INDONESIA = "ID";
    private static final String KEYWORD = "keyword";
    private final FlightRepository flightRepository;

    @Inject
    public FlightAirportPickerUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public static RequestParams createRequestParams(String text) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEYWORD, text);
        return requestParams;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return flightRepository
                .getAirportList(requestParams.getString(KEYWORD, ""))
                .map(new Func1<List<FlightAirportDB>, List<Visitable>>() {
                    @Override
                    public List<Visitable> call(List<FlightAirportDB> airports) {
                        List<Visitable> visitables = new ArrayList<>();
                        List<Visitable> result = new ArrayList<>();
                        FlightCountryAirportViewModel negara = null;

                        for (int i = 0; i < airports.size(); i++) {
                            FlightAirportDB airport = airports.get(i);
                            if (negara == null || !negara.getCountryId().equalsIgnoreCase(airport.getCountryId())) {
                                negara = new FlightCountryAirportViewModel();
                                negara.setCountryId(airport.getCountryId());
                                negara.setCountryName(airport.getCountryName());
                                negara.setAirports(new ArrayList<FlightAirportViewModel>());
                                if (negara.getCountryId().equalsIgnoreCase(ID_COUNTRY_INDONESIA)) {
                                    result.add(negara);
                                } else {
                                    visitables.add(negara);
                                }
                            }

                            FlightAirportViewModel airportViewModel = new FlightAirportViewModel();
                            airportViewModel.setAirportName(airport.getAirportName());
                            airportViewModel.setCountryName(negara.getCountryName());
                            if (airport.getAirportId() != null && airport.getAirportId().length() > 0) {
                                airportViewModel.setAirportCode(airport.getAirportId());
                            } else {
                                airportViewModel.setCityAirports(airport.getAirportIds().split(","));
                            }
                            airportViewModel.setCityCode(airport.getCityCode());
                            airportViewModel.setCityId(airport.getCityId());
                            airportViewModel.setCityName(airport.getCityName());
                            if (negara.getCountryId().equalsIgnoreCase(ID_COUNTRY_INDONESIA)) {
                                result.add(airportViewModel);
                            } else {
                                visitables.add(airportViewModel);
                            }
                        }
                        result.addAll(visitables);
                        return result;
                    }
                });
    }

    public RequestParams createRequestParam(String text) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(KEYWORD, text);
        return requestParams;
    }
}
