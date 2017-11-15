package com.tokopedia.flight.search.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.domain.FlightSearchStatisticUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.domain.FlightSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.resultstatistics.FlightSearchStatisticModel;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    private FlightSearchWithSortUseCase flightSearchWithSortUseCase;
    private FlightSortUseCase flightSortUseCase;
    private FlightSearchStatisticUseCase flightSearchStatisticUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchWithSortUseCase flightSearchWithSortUseCase,
                                 FlightSortUseCase flightSortUseCase,
                                 FlightSearchStatisticUseCase flightSearchStatisticUseCase) {
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
        this.flightSortUseCase = flightSortUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
    }

    public void searchAndSortFlight(FlightSearchPassDataViewModel flightSearchPassDataViewModel,
                                    boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel,
                                    @FlightSortOption int sortOptionId) {
        flightSearchWithSortUseCase.execute(FlightSearchUseCase.generateRequestParams(
                flightSearchPassDataViewModel,
                isReturning, isFromCache, flightFilterModel,
                sortOptionId),
                getSubscriberSearchFlight(sortOptionId));
    }

    public void getFlightStatistic(boolean isReturning) {
        flightSearchStatisticUseCase.execute(FlightSearchUseCase.generateRequestParams(
                null,
                isReturning, true, null, FlightSortOption.NO_PREFERENCE),
                getSubscriberSearchStatisticFlight());
    }

    public void sortFlight(List<FlightSearchViewModel> flightSearchViewModelList,
                           @FlightSortOption int sortOptionId) {
        flightSortUseCase.withList(flightSearchViewModelList).execute(null,
                getSubscriberSortFlight(sortOptionId));
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchWithSortUseCase.unsubscribe();
        flightSearchStatisticUseCase.unsubscribe();
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

    private Subscriber<List<FlightSearchViewModel>> getSubscriberSortFlight(final int sortOptionId) {
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

    public Subscriber<FlightSearchStatisticModel> getSubscriberSearchStatisticFlight() {
        return new Subscriber<FlightSearchStatisticModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(FlightSearchStatisticModel statisticModel) {
                getView().onSuccessGetStatistic(statisticModel);
            }
        };
    }
}
