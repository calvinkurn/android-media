package com.tokopedia.flight.search.domain;

import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.search.data.db.model.FlightMetaDataDB;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchWithMetaViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by alvarisi on 11/1/17.
 */

public class FlightSearchMetaUseCase extends UseCase<FlightSearchWithMetaViewModel> {
    private FlightRepository flightRepository;
    private FlightSearchUseCase flightSearchUseCase;
    private FlightSearchGetMetaUseCase flightSearchGetMetaUseCase;

    @Inject
    public FlightSearchMetaUseCase(FlightRepository flightRepository, FlightSearchUseCase flightSearchUseCase, FlightSearchGetMetaUseCase flightSearchGetMetaUseCase) {
        this.flightRepository = flightRepository;
        this.flightSearchUseCase = flightSearchUseCase;
        this.flightSearchGetMetaUseCase = flightSearchGetMetaUseCase;
    }

    @Override
    public Observable<FlightSearchWithMetaViewModel> createObservable(final RequestParams requestParams) {
        /*return flightRepository.refreshAirlines().flatMap(new Func1<List<FlightAirlineDB>, Observable<FlightSearchWithMetaViewModel>>() {
            @Override
            public Observable<FlightSearchWithMetaViewModel> call(List<FlightAirlineDB> flightAirlineDBS) {
                return getSearchMetaResultObservable(requestParams);
            }
        });*/
        return getSearchMetaResultObservable(requestParams);
    }

    private Observable<FlightSearchWithMetaViewModel> getSearchMetaResultObservable(final RequestParams requestParams) {
        return flightSearchUseCase.createObservable(requestParams).flatMap(new Func1<List<FlightSearchViewModel>, Observable<FlightSearchWithMetaViewModel>>() {
            @Override
            public Observable<FlightSearchWithMetaViewModel> call(List<FlightSearchViewModel> flightSearchViewModelList) {
                FlightSearchApiRequestModel flightSearchApiRequestModel = FlightSearchParamUtil.getInitialPassData(requestParams);
                String depAirport = flightSearchApiRequestModel.getDepAirport();
                String arrAirport = flightSearchApiRequestModel.getArrAirport();
                String date = flightSearchApiRequestModel.getDate();
                Observable<FlightMetaDataDB> observableFlightMeta = flightSearchGetMetaUseCase.createObservable(FlightSearchGetMetaUseCase.generateRequestParams(depAirport, arrAirport, date));

                return Observable.zip(Observable.just(flightSearchViewModelList),
                        observableFlightMeta, new Func2<List<FlightSearchViewModel>, FlightMetaDataDB, FlightSearchWithMetaViewModel>() {
                            @Override
                            public FlightSearchWithMetaViewModel call(List<FlightSearchViewModel> flightSearchViewModelList,
                                                                                        FlightMetaDataDB flightMetaDataDB) {
                                return new FlightSearchWithMetaViewModel(flightSearchViewModelList, flightMetaDataDB);
                            }
                        });
            }
        });
    }

    @Override
    public void unsubscribe() {
        super.unsubscribe();
        flightSearchUseCase.unsubscribe();
        flightSearchGetMetaUseCase.unsubscribe();
    }
}
