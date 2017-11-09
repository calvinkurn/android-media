package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightFilterCountUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.view.FlightFilterCountView;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightFilterCountPresenter extends BaseDaggerPresenter<FlightFilterCountView> {

    private final FlightFilterCountUseCase flightFilterCountUseCase;

    @Inject
    public FlightFilterCountPresenter(FlightFilterCountUseCase flightFilterCountUseCase) {
        this.flightFilterCountUseCase = flightFilterCountUseCase;
    }

    //TODO params
    public void getFlightCount(boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel) {
        flightFilterCountUseCase.execute(FlightSearchUseCase.generateRequestParams(isReturning, isFromCache, flightFilterModel,
                FlightSortOption.NO_PREFERENCE),
                getSubscriberFlightCount());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightFilterCountUseCase.unsubscribe();
    }

    private Subscriber<Integer> getSubscriberFlightCount() {
        return new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onErrorGetCount(e);
            }

            @Override
            public void onNext(Integer integer) {
                getView().onSuccessGetCount(integer);
            }
        };
    }
}
