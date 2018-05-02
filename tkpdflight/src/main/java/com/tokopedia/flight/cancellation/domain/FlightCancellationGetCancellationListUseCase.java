package com.tokopedia.flight.cancellation.domain;

import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.cancellation.domain.mapper.FlightOrderEntityToCancellationListMapper;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.orderlist.data.cache.FlightOrderDataCacheSource;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.domain.model.FlightOrderJourney;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func3;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationGetCancellationListUseCase extends UseCase<List<FlightCancellationListViewModel>> {

    private FlightOrderDataCacheSource flightOrderDataCacheSource;
    private FlightOrderEntityToCancellationListMapper flightOrderEntityToCancellationListMapper;
    private FlightRepository flightRepository;

    @Inject
    public FlightCancellationGetCancellationListUseCase(FlightOrderDataCacheSource flightOrderDataCacheSource,
                                                        FlightOrderEntityToCancellationListMapper flightOrderEntityToCancellationListMapper,
                                                        FlightRepository flightRepository) {
        this.flightOrderDataCacheSource = flightOrderDataCacheSource;
        this.flightOrderEntityToCancellationListMapper = flightOrderEntityToCancellationListMapper;
        this.flightRepository = flightRepository;
    }

    @Override
    public Observable<List<FlightCancellationListViewModel>> createObservable(RequestParams requestParams) {
        return flightOrderDataCacheSource.getCache()
                .flatMap(new Func1<OrderEntity, Observable<List<FlightCancellationListViewModel>>>() {
                    @Override
                    public Observable<List<FlightCancellationListViewModel>> call(OrderEntity orderEntity) {
                        return Observable.just(flightOrderEntityToCancellationListMapper.transform(orderEntity));
                    }
                })
                .flatMap(new Func1<List<FlightCancellationListViewModel>, Observable<List<FlightCancellationListViewModel>>>() {
                    @Override
                    public Observable<List<FlightCancellationListViewModel>> call(List<FlightCancellationListViewModel> flightCancellationListViewModels) {
                        return Observable.from(flightCancellationListViewModels)
                                .flatMap(new Func1<FlightCancellationListViewModel, Observable<FlightCancellationListViewModel>>() {
                                    @Override
                                    public Observable<FlightCancellationListViewModel> call(FlightCancellationListViewModel flightCancellationListViewModel) {
                                        return Observable.zip(
                                                Observable.just(flightCancellationListViewModel),
                                                Observable.from(flightCancellationListViewModel.getCancellations().getJourneys())
                                                        .flatMap(new Func1<FlightOrderJourney, Observable<FlightOrderJourney>>() {
                                                            @Override
                                                            public Observable<FlightOrderJourney> call(FlightOrderJourney flightOrderJourney) {
                                                                return Observable.zip(
                                                                        Observable.just(flightOrderJourney),
                                                                        flightRepository.getAirportById(flightOrderJourney.getDepartureAiportId()),
                                                                        flightRepository.getAirportById(flightOrderJourney.getArrivalAirportId()),
                                                                        new Func3<FlightOrderJourney, FlightAirportDB, FlightAirportDB, FlightOrderJourney>() {
                                                                            @Override
                                                                            public FlightOrderJourney call(FlightOrderJourney flightOrderJourney, FlightAirportDB departureAirport, FlightAirportDB arrivalAirport) {
                                                                                if (departureAirport != null) {
                                                                                    flightOrderJourney.setDepartureCity(departureAirport.getCityName());
                                                                                    flightOrderJourney.setDepartureCityCode(departureAirport.getCityCode());
                                                                                }

                                                                                if (arrivalAirport != null) {
                                                                                    flightOrderJourney.setArrivalCity(arrivalAirport.getCityName());
                                                                                    flightOrderJourney.setArrivalCityCode(arrivalAirport.getCityCode());
                                                                                }

                                                                                return flightOrderJourney;
                                                                            }
                                                                        }
                                                                );
                                                            }
                                                        })
                                                        .toList(),
                                                new Func2<FlightCancellationListViewModel, List<FlightOrderJourney>, FlightCancellationListViewModel>() {
                                                    @Override
                                                    public FlightCancellationListViewModel call(FlightCancellationListViewModel flightCancellationListViewModel, List<FlightOrderJourney> flightOrderJourneyList) {
                                                        flightCancellationListViewModel.getCancellations().setJourneys(flightOrderJourneyList);
                                                        return flightCancellationListViewModel;
                                                    }
                                                }
                                        );
                                    }
                                })
                                .toList();
                    }
                });
    }

    public RequestParams createEmptyRequest() {
        return RequestParams.EMPTY;
    }

}
