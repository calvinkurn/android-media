package com.tokopedia.flight.airport.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportPickerPresenterImpl extends BaseDaggerPresenter<FlightAirportPickerView> implements FlightAirportPickerPresenter {

    private final FlightAirportPickerUseCase flightAirportPickerUseCase;

    public FlightAirportPickerPresenterImpl(FlightAirportPickerUseCase flightAirportPickerUseCase) {
        this.flightAirportPickerUseCase = flightAirportPickerUseCase;
    }

    @Override
    public void getAirportList(String text) {
        flightAirportPickerUseCase.execute(FlightAirportPickerUseCase.createRequestParams(text), getSubscriberGetAirportList());
    }

    public Subscriber<List<FlightAirportDB>> getSubscriberGetAirportList() {
        return new Subscriber<List<FlightAirportDB>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onLoadSearchError(e);
                }
            }

            @Override
            public void onNext(List<FlightAirportDB> flightAirportDBs) {
                getView().onSearchLoaded(flightAirportDBs, flightAirportDBs.size());
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        flightAirportPickerUseCase.unsubscribe();
    }
}
