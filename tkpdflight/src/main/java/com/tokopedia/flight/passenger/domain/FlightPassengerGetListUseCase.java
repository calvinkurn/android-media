package com.tokopedia.flight.passenger.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.passenger.data.db.model.FlightPassengerDb;
import com.tokopedia.flight.passenger.domain.model.ListPassengerViewModelMapper;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func3;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightPassengerGetListUseCase extends UseCase<List<FlightBookingPassengerViewModel>> {

    private static final String PARAM_PASSENGER_ID = "PARAM_PASSENGER_ID";
    private static final String DEFAULT_STRING_VALUE = "";

    private final FlightRepository flightRepository;
    private final ListPassengerViewModelMapper listPassengerViewModelMapper;

    @Inject
    public FlightPassengerGetListUseCase(FlightRepository flightRepository, ListPassengerViewModelMapper listPassengerViewModelMapper) {
        this.flightRepository = flightRepository;
        this.listPassengerViewModelMapper = listPassengerViewModelMapper;
    }

    @Override
    public Observable<List<FlightBookingPassengerViewModel>> createObservable(RequestParams requestParams) {
        return flightRepository.getPassengerList(requestParams.getString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE))
                .flatMap(new Func1<List<FlightPassengerDb>, Observable<List<FlightBookingPassengerViewModel>>>() {
                    @Override
                    public Observable<List<FlightBookingPassengerViewModel>> call(List<FlightPassengerDb> flightPassengerDbs) {
                        return Observable.just(listPassengerViewModelMapper.transform(flightPassengerDbs));
                    }
                })
                .flatMap(new Func1<List<FlightBookingPassengerViewModel>, Observable<List<FlightBookingPassengerViewModel>>>() {
                    @Override
                    public Observable<List<FlightBookingPassengerViewModel>> call(final List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList) {
                        return Observable.from(flightBookingPassengerViewModelList)
                                .flatMap(new Func1<FlightBookingPassengerViewModel, Observable<FlightBookingPassengerViewModel>>() {
                                    @Override
                                    public Observable<FlightBookingPassengerViewModel> call(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
                                        if (flightBookingPassengerViewModel.getPassportNationality() != null &&
                                                flightBookingPassengerViewModel.getPassportIssuerCountry() != null) {
                                            return getPassportData(flightBookingPassengerViewModel);
                                        } else {
                                            return Observable.just(flightBookingPassengerViewModel);
                                        }
                                    }
                                })
                                .toList();
                    }
                });
    }

    public RequestParams createEmptyRequestParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, DEFAULT_STRING_VALUE);
        return requestParams;
    }

    public RequestParams generateRequestParams(String passengerId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_PASSENGER_ID, passengerId);
        return requestParams;
    }

    private Observable<FlightBookingPassengerViewModel> getPassportData(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        return Observable.zip(
                Observable.just(flightBookingPassengerViewModel),
                flightRepository.getPhoneCodeById(flightBookingPassengerViewModel.getPassportNationality().getCountryId()),
                flightRepository.getPhoneCodeById(flightBookingPassengerViewModel.getPassportIssuerCountry().getCountryId()),
                new Func3<FlightBookingPassengerViewModel, FlightAirportDB, FlightAirportDB, FlightBookingPassengerViewModel>() {
                    @Override
                    public FlightBookingPassengerViewModel call(FlightBookingPassengerViewModel flightBookingPassengerViewModel, FlightAirportDB nationality, FlightAirportDB issuerCountry) {
                        FlightBookingPhoneCodeViewModel passportNationality = new FlightBookingPhoneCodeViewModel();
                        passportNationality.setCountryId(nationality.getCountryId());
                        passportNationality.setCountryName(nationality.getCountryName());
                        passportNationality.setCountryPhoneCode(String.valueOf(nationality.getPhoneCode()));

                        FlightBookingPhoneCodeViewModel passportIssuerCountry = new FlightBookingPhoneCodeViewModel();
                        passportIssuerCountry.setCountryId(issuerCountry.getCountryId());
                        passportIssuerCountry.setCountryName(issuerCountry.getCountryName());
                        passportIssuerCountry.setCountryPhoneCode(String.valueOf(issuerCountry.getPhoneCode()));

                        flightBookingPassengerViewModel.setPassportNationality(passportNationality);
                        flightBookingPassengerViewModel.setPassportIssuerCountry(passportIssuerCountry);
                        return flightBookingPassengerViewModel;
                    }
                }
        );
    }
}
