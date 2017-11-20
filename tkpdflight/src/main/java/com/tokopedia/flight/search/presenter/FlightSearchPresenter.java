package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightSearchSortWithMetaUseCase;
import com.tokopedia.flight.search.domain.FlightSearchStatisticUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.domain.FlightSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchWithMetaViewModel;
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
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightSearchSortWithMetaUseCase flightSearchSortWithMetaUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchWithSortUseCase flightSearchWithSortUseCase,
                                 FlightSortUseCase flightSortUseCase,
                                 FlightSearchStatisticUseCase flightSearchStatisticUseCase,
                                 FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                 FlightSearchSortWithMetaUseCase flightSearchSortWithMetaUseCase) {
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
        this.flightSortUseCase = flightSortUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightSearchSortWithMetaUseCase = flightSearchSortWithMetaUseCase;
    }

    public void searchAndSortFlight(FlightSearchApiRequestModel flightSearchApiRequestModel,
                                    boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel,
                                    @FlightSortOption int sortOptionId) {
        if (isFromCache) {
            flightSearchWithSortUseCase.execute(FlightSearchUseCase.generateRequestParams(
                    flightSearchApiRequestModel,
                    isReturning, true, flightFilterModel,
                    sortOptionId),
                    getSubscriberSearchFlightCache(sortOptionId));
        } else {
            flightSearchSortWithMetaUseCase.execute(FlightSearchUseCase.generateRequestParams(
                    flightSearchApiRequestModel,
                    isReturning, false, flightFilterModel,
                    sortOptionId),
                    getSubscriberSearchFlightCloud(sortOptionId));
        }
    }

    public void getFlightStatistic(boolean isReturning) {
        flightSearchStatisticUseCase.execute(FlightSearchUseCase.generateRequestParams(
                null,
                isReturning, true, null, FlightSortOption.NO_PREFERENCE),
                getSubscriberSearchStatisticFlight());
    }

    public void sortFlight(List<FlightSearchViewModel> flightSearchViewModelList,
                           @FlightSortOption int sortOptionId) {
        flightSortUseCase.withList(flightSearchViewModelList).execute(FlightSearchUseCase.generateRequestParams(
                null,
                false, true, null,
                sortOptionId),
                getSubscriberSortFlight(sortOptionId));
    }

    public void getDetailDepartureFlight(String selectedFlightDeparture) {
        flightBookingGetSingleResultUseCase.execute(flightBookingGetSingleResultUseCase.createRequestParam(false, selectedFlightDeparture), getSubscriberDetailDepartureFlight());
    }

    @Override
    public void detachView() {
        super.detachView();
        flightSearchWithSortUseCase.unsubscribe();
        flightSearchStatisticUseCase.unsubscribe();
        flightBookingGetSingleResultUseCase.unsubscribe();
        flightSortUseCase.unsubscribe();
        flightSearchSortWithMetaUseCase.unsubscribe();
    }

    private Subscriber<FlightSearchViewModel> getSubscriberDetailDepartureFlight() {
        return new Subscriber<FlightSearchViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetDetailFlightDeparture(e);
                }
            }

            @Override
            public void onNext(FlightSearchViewModel flightSearchViewModel) {
                getView().onSuccessGetDetailFlightDeparture(flightSearchViewModel);
            }
        };
    }

    private Subscriber<List<FlightSearchViewModel>> getSubscriberSearchFlightCache(final int sortOptionId) {
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
                getView().onSuccessGetDataFromCache(flightSearchViewModels);
                getView().setSelectedSortItem(sortOptionId);
            }
        };
    }

    private Subscriber<FlightSearchWithMetaViewModel> getSubscriberSearchFlightCloud(final int sortOptionId) {
        return new Subscriber<FlightSearchWithMetaViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().onLoadSearchError(e);
            }

            @Override
            public void onNext(FlightSearchWithMetaViewModel flightSearchWithMetaViewModel) {
                getView().onSuccessGetDataFromCloud(flightSearchWithMetaViewModel.getFlightSearchViewModelList(), flightSearchWithMetaViewModel.getFlightMetaDataDB());
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
                getView().onSuccessGetDataFromCache(flightSearchViewModels);
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
