package com.tokopedia.flight.search.presenter;

import android.view.View;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.subscriber.OnNextSubscriber;
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
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightSearchPresenter extends BaseDaggerPresenter<FlightSearchView> {

    public static final int DELAY_HORIZONTAL_PROGRESS = 500;

    private FlightSearchWithSortUseCase flightSearchWithSortUseCase;
    private FlightSortUseCase flightSortUseCase;
    private FlightSearchStatisticUseCase flightSearchStatisticUseCase;
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightSearchSortWithMetaUseCase flightSearchSortWithMetaUseCase;
    private CompositeSubscription compositeSubscription;

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

    public void searchAndSortFlightWithDelay(final FlightSearchApiRequestModel flightSearchApiRequestModel,
                                             final boolean isReturning, final boolean isFromCache, final FlightFilterModel flightFilterModel,
                                             @FlightSortOption final int sortOptionId, int delayInSecond) {
        Subscription subscription = Observable.timer(delayInSecond, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        searchAndSortFlight(flightSearchApiRequestModel,
                                isReturning, isFromCache, flightFilterModel, sortOptionId);
                    }
                });
        addSubscription(subscription);
    }

    public void setDelayHorizontalProgress(){
        Subscription subscription = Observable.timer(DELAY_HORIZONTAL_PROGRESS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        getView().hideHorizontalProgress();
                    }
                });
        addSubscription(subscription);
    }

    private void addSubscription(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
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
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
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
