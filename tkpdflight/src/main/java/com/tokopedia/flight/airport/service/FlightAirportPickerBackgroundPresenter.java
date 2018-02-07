package com.tokopedia.flight.airport.service;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerBackgroundUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public class FlightAirportPickerBackgroundPresenter extends BaseDaggerPresenter<FlightAirportPickerBackgroundContract.View>
        implements FlightAirportPickerBackgroundContract.Presenter {

    private final FlightAirportPickerBackgroundUseCase flightAirportPickerBackgroundUseCase;

    @Inject
    public FlightAirportPickerBackgroundPresenter(FlightAirportPickerBackgroundUseCase flightAirportPickerBackgroundUseCase) {
        this.flightAirportPickerBackgroundUseCase = flightAirportPickerBackgroundUseCase;
    }

    @Override
    public void getAirportListCloud(long versionAirport) {
        flightAirportPickerBackgroundUseCase.execute(flightAirportPickerBackgroundUseCase.createRequestParams(versionAirport), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onGetAirportError(e);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().onGetAirport(isSuccess);
            }
        });
    }
}
