package com.tokopedia.flight.booking.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingGetPhoneCodeUseCase extends UseCase<List<FlightBookingPhoneCodeViewModel>> {
    private static final String PARAM_QUERY = "query";
    private static final String DEFAULT_PARAM = "";


    private final FlightRepository flightRepository;

    @Inject
    public FlightBookingGetPhoneCodeUseCase(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightBookingPhoneCodeViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPhoneCodeList(requestParams.getString(PARAM_QUERY, DEFAULT_PARAM))
                .flatMap(new Func1<List<FlightAirportDB>, Observable<List<FlightBookingPhoneCodeViewModel>>>() {
                    @Override
                    public Observable<List<FlightBookingPhoneCodeViewModel>> call(List<FlightAirportDB> flightAirportDBs) {
                        List<FlightBookingPhoneCodeViewModel> flightBookingPhoneCodeViewModels = new ArrayList<>();
                        for (FlightAirportDB flightAirportDB : flightAirportDBs) {
                            FlightBookingPhoneCodeViewModel flightBookingPhoneCodeViewModel = new FlightBookingPhoneCodeViewModel();
                            boolean isCountryIdDuplicate = false;
                            for (FlightBookingPhoneCodeViewModel flightBookingPhoneCodeTemp : flightBookingPhoneCodeViewModels) {
                                if (flightBookingPhoneCodeTemp.getCountryId().equals(flightAirportDB.getCountryId())) {
                                    isCountryIdDuplicate = true;
                                }
                            }

                            if (!isCountryIdDuplicate) {
                                flightBookingPhoneCodeViewModel.setCountryId(flightAirportDB.getCountryId());
                                flightBookingPhoneCodeViewModel.setCountryName(flightAirportDB.getCountryName());
                                flightBookingPhoneCodeViewModel.setCountryPhoneCode(String.valueOf(flightAirportDB.getPhoneCode()));
                                flightBookingPhoneCodeViewModels.add(flightBookingPhoneCodeViewModel);
                            }
                        }
                        return Observable.just(flightBookingPhoneCodeViewModels);
                    }
                });
    }

    public RequestParams createRequest(String query) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, query);
        return requestParams;
    }
}
