package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    private final FlightSearchUseCase flightSearchUseCase;
    private FlightSearchWithSortUseCase flightSearchWithSortUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchUseCase flightSearchUseCase,
                                 FlightSearchWithSortUseCase flightSearchWithSortUseCase) {
        this.flightSearchUseCase = flightSearchUseCase;
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
    }

    //TODO params
    public void searchDepartureFlight(boolean isFromCache) {
        flightSearchUseCase.execute(FlightSearchUseCase.generateRequestParams(false, isFromCache),
                getSubscriberSearchFlight());
    }

    public void searchReturningFlight(boolean isFromCache) {
        flightSearchUseCase.execute(FlightSearchUseCase.generateRequestParams(true, isFromCache),
                getSubscriberSearchFlight());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchUseCase.unsubscribe();
    }

    private Subscriber<List<FlightSearchViewModel>> getSubscriberSearchFlight() {
        return new Subscriber<List<FlightSearchViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {

                getView().onSearchLoaded(flightSearchViewModels, flightSearchViewModels.size());
            }
        };
    }

    public void onSortItemSelected(final int itemId) {
        if (!isViewAttached()) return;
        getView().showSortRouteLoading();
        RequestParams requestParams = getView().getSearchFlightRequestParam();
        requestParams.putAll(flightSearchWithSortUseCase.createRequestParam(itemId).getParameters());
        flightSearchWithSortUseCase.execute(requestParams, new Subscriber<List<FlightSearchViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().onLoadSearchError(e);
                    getView().hideSortRouteLoading();
                }
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {
                if (isViewAttached()) {
                    getView().hideSortRouteLoading();
                    getView().onSearchLoaded(flightSearchViewModels, flightSearchViewModels.size());
                    getView().setSelectedSortItem(itemId);
                }
            }
        });
    }
}
