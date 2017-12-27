package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.data.domain.DeleteFlightCacheUseCase;
import com.tokopedia.flight.common.subscriber.OnNextSubscriber;
import com.tokopedia.flight.common.util.FlightErrorUtil;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightSearchMetaUseCase;
import com.tokopedia.flight.search.domain.FlightSearchStatisticUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.domain.FlightSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView2;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchWithMetaViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
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

public class FlightSearchPresenter2 extends BaseDaggerPresenter<FlightSearchView2> {

    public static final int DELAY_HORIZONTAL_PROGRESS = 500;

    private FlightSearchWithSortUseCase flightSearchWithSortUseCase;
    private FlightSortUseCase flightSortUseCase;
    private FlightSearchStatisticUseCase flightSearchStatisticUseCase;
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private FlightSearchMetaUseCase flightSearchMetaUseCase;
    private CompositeSubscription compositeSubscription;
    private DeleteFlightCacheUseCase deleteFlightCacheUseCase;

    @Inject
    public FlightSearchPresenter2(FlightSearchWithSortUseCase flightSearchWithSortUseCase,
                                  FlightSortUseCase flightSortUseCase,
                                  FlightSearchStatisticUseCase flightSearchStatisticUseCase,
                                  FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                  FlightSearchMetaUseCase flightSearchMetaUseCase,
                                  DeleteFlightCacheUseCase deleteFlightCacheUseCase) {
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
        this.flightSortUseCase = flightSortUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightSearchMetaUseCase = flightSearchMetaUseCase;
        this.deleteFlightCacheUseCase = deleteFlightCacheUseCase;
    }

    public void searchAndSortFlight(FlightSearchApiRequestModel flightSearchApiRequestModel,
                                    boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel,
                                    @FlightSortOption int sortOptionId) {
        getView().removeToolbarElevation();
        if (isFromCache) {
            flightSearchWithSortUseCase.execute(FlightSearchUseCase.generateRequestParams(
                    flightSearchApiRequestModel,
                    isReturning, true, flightFilterModel,
                    sortOptionId),
                    getSubscriberSearchFlightCache(sortOptionId));
        } else {
            flightSearchMetaUseCase.execute(FlightSearchUseCase.generateRequestParams(
                    flightSearchApiRequestModel,
                    isReturning, false, null,
                    FlightSortOption.NO_PREFERENCE),
                    getSubscriberSearchFlightCloud());
        }
    }

    public void searchAndSortFlightWithDelay(final FlightSearchApiRequestModel flightSearchApiRequestModel,
                                             final boolean isReturning, int delayInSecond) {
        getView().removeToolbarElevation();
        Subscription subscription = Observable.timer(delayInSecond, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new OnNextSubscriber<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if (isViewAttached()) {
                            searchAndSortFlight(flightSearchApiRequestModel, isReturning,
                                    false, null,
                                    FlightSortOption.NO_PREFERENCE);
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void setDelayHorizontalProgress() {
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

    public void deleteFlightCache(boolean isReturning) {
        deleteFlightCacheUseCase.execute(DeleteFlightCacheUseCase.createRequestParam(isReturning), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorDeleteFlightCache(e);
                }
            }

            @Override
            public void onNext(Boolean aBoolean) {
                getView().onSuccessDeleteFlightCache();
            }
        });
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
        getView().removeToolbarElevation();
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
        flightSearchMetaUseCase.unsubscribe();
        deleteFlightCacheUseCase.unsubscribe();
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
                if (isViewAttached()) {
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
                getView().showGetListError(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {
                getView().hideSortRouteLoading();
                getView().onSuccessGetDataFromCache(flightSearchViewModels);
                getView().setSelectedSortItem(sortOptionId);
            }
        };
    }

    private Subscriber<FlightSearchWithMetaViewModel> getSubscriberSearchFlightCloud() {
        return new Subscriber<FlightSearchWithMetaViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showGetListError(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
            }

            @Override
            public void onNext(FlightSearchWithMetaViewModel flightSearchWithMetaViewModel) {
                List<FlightSearchViewModel> flightSearchViewModelList = flightSearchWithMetaViewModel.getFlightSearchViewModelList();
                boolean dataFromCloudEmpty = (flightSearchWithMetaViewModel == null || flightSearchViewModelList.size() == 0);
                getView().onSuccessGetDataFromCloud(dataFromCloudEmpty, flightSearchWithMetaViewModel.getFlightMetaDataDB());
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
                e.printStackTrace();
                getView().hideSortRouteLoading();
                getView().showGetListError(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
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
                e.printStackTrace();
                getView().showGetListError(FlightErrorUtil.getMessageFromException(getView().getActivity(), e));
            }

            @Override
            public void onNext(FlightSearchStatisticModel statisticModel) {
                getView().onSuccessGetStatistic(statisticModel);
            }
        };
    }
}
