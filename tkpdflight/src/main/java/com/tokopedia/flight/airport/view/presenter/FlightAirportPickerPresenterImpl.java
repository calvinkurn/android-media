package com.tokopedia.flight.airport.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportVersionCheckUseCase;

import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportPickerPresenterImpl extends BaseDaggerPresenter<FlightAirportPickerView> implements FlightAirportPickerPresenter {

    private final FlightAirportPickerUseCase flightAirportPickerUseCase;
    private FlightAirportVersionCheckUseCase flightAirportVersionCheckUseCase;

    public FlightAirportPickerPresenterImpl(FlightAirportPickerUseCase flightAirportPickerUseCase,
                                            FlightAirportVersionCheckUseCase flightAirportVersionCheckUseCase) {
        this.flightAirportPickerUseCase = flightAirportPickerUseCase;
        this.flightAirportVersionCheckUseCase = flightAirportVersionCheckUseCase;
    }

    @Override
    public void getAirportList(String text, boolean isFirstTime) {
        getView().showGetAirportListLoading();
        flightAirportPickerUseCase.execute(FlightAirportPickerUseCase.createRequestParams(text), getSubscriberGetAirportList());
    }

    @Override
    public void checkAirportVersion(long currentVersion) {
        flightAirportVersionCheckUseCase.execute(flightAirportVersionCheckUseCase.createRequestParams(currentVersion),
                getSubscriberCheckAirportVersion());
    }

    private Subscriber<Boolean> getSubscriberCheckAirportVersion() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isShouldUpdate) {
                if (isShouldUpdate) {
                    getView().updateAirportListOnBackground();
                }
            }
        };
    }

    public Subscriber<List<FlightAirportDB>> getSubscriberGetAirportList() {
        return new Subscriber<List<FlightAirportDB>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideGetAirportListLoading();
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(List<FlightAirportDB> flightAirportDBs) {
                getView().hideGetAirportListLoading();
                getView().renderList(flightAirportDBs);
            }
        };
    }

    @Override
    public void detachView() {
        super.detachView();
        flightAirportPickerUseCase.unsubscribe();
    }
}
