package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    private FlightSearchWithSortUseCase flightSearchWithSortUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchWithSortUseCase flightSearchWithSortUseCase) {
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
    }

    public void searchAndSortFlight(boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel,
                                    @FlightSortOption int sortOptionId) {
        flightSearchWithSortUseCase.execute(FlightSearchUseCase.generateRequestParams(isReturning, isFromCache, flightFilterModel,
                sortOptionId),
                getSubscriberSearchFlight(sortOptionId));
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchWithSortUseCase.unsubscribe();
    }

    private Subscriber<List<FlightSearchViewModel>> getSubscriberSearchFlight(final int sortOptionId) {
        return new Subscriber<List<FlightSearchViewModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().hideSortRouteLoading();
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {
                getView().hideSortRouteLoading();
                getView().onSearchLoaded(flightSearchViewModels, flightSearchViewModels.size());
                getView().setSelectedSortItem(sortOptionId);
            }
        };
    }

}
