package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.data.domain.DeleteFlightCacheUseCase;
import com.tokopedia.flight.common.subscriber.OnNextSubscriber;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.constant.FlightSortOption;
import com.tokopedia.flight.search.domain.FlightAirlineHardRefreshUseCase;
import com.tokopedia.flight.search.domain.FlightSearchExpiredUseCase;
import com.tokopedia.flight.search.domain.FlightSearchMetaUseCase;
import com.tokopedia.flight.search.domain.FlightSearchStatisticUseCase;
import com.tokopedia.flight.search.domain.FlightSearchUseCase;
import com.tokopedia.flight.search.domain.FlightSearchWithSortUseCase;
import com.tokopedia.flight.search.domain.FlightSortUseCase;
import com.tokopedia.flight.search.view.FlightSearchView;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchWithMetaViewModel;
import com.tokopedia.flight.search.view.model.filter.FlightFilterModel;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;
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
    private FlightSearchMetaUseCase flightSearchMetaUseCase;
    private CompositeSubscription compositeSubscription;
    private DeleteFlightCacheUseCase deleteFlightCacheUseCase;
    private FlightAnalytics flightAnalytics;
    private FlightSearchExpiredUseCase flightSearchExpiredUseCase;
    private FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase;

    @Inject
    public FlightSearchPresenter(FlightSearchWithSortUseCase flightSearchWithSortUseCase,
                                 FlightSortUseCase flightSortUseCase,
                                 FlightSearchStatisticUseCase flightSearchStatisticUseCase,
                                 FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase,
                                 FlightSearchMetaUseCase flightSearchMetaUseCase,
                                 DeleteFlightCacheUseCase deleteFlightCacheUseCase,
                                 FlightAnalytics flightAnalytics,
                                 FlightSearchExpiredUseCase flightSearchExpiredUseCase,
                                 FlightAirlineHardRefreshUseCase flightAirlineHardRefreshUseCase) {
        this.flightSearchWithSortUseCase = flightSearchWithSortUseCase;
        this.flightSortUseCase = flightSortUseCase;
        this.flightSearchStatisticUseCase = flightSearchStatisticUseCase;
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.flightSearchMetaUseCase = flightSearchMetaUseCase;
        this.deleteFlightCacheUseCase = deleteFlightCacheUseCase;
        this.flightAnalytics = flightAnalytics;
        this.flightSearchExpiredUseCase = flightSearchExpiredUseCase;
        this.flightAirlineHardRefreshUseCase = flightAirlineHardRefreshUseCase;
    }

    public void searchAndSortFlight(final FlightSearchApiRequestModel flightSearchApiRequestModel,
                                    final boolean isReturning, boolean isFromCache, FlightFilterModel flightFilterModel,
                                    @FlightSortOption int sortOptionId) {
        if (isViewAttached()) {
            getView().removeToolbarElevation();
        }

        if (isFromCache) {
            flightSearchWithSortUseCase.execute(
                    FlightSearchUseCase.generateRequestParams(
                            flightSearchApiRequestModel,
                            isReturning,
                            true,
                            flightFilterModel,
                            sortOptionId),
                    getSubscriberSortFlight(sortOptionId));
        } else {
                flightSearchMetaUseCase.execute(
                        FlightSearchUseCase.generateRequestParams(
                                flightSearchApiRequestModel,
                                isReturning,
                                false,
                                null,
                                FlightSortOption.NO_PREFERENCE),
                        getSubscriberSearchFlightCloud());
        }
    }

    public void searchAndSortFlightWithDelay(final FlightSearchApiRequestModel flightSearchApiRequestModel,
                                             final boolean isReturning, int delayInSecond) {
        getView().removeToolbarElevation();
        Subscription subscription = Observable.timer(delayInSecond, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
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
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
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

    public void checkCacheExpired() {
        flightSearchExpiredUseCase.execute(flightSearchExpiredUseCase.createRequestParams(
                getView().isReturning()),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().finishFragment();
                        } else {
                            if (getView().isNeedRefreshFromCache()) {
                                getView().reloadDataFromCache();
                                getView().setUIMarkFilter();
                                getView().setNeedRefreshFromCache(false);
                            }
                            getView().loadInitialData();
                        }
                    }
                });
    }

    private void addSubscription(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
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

    public void onSuccessDateChanged(int year, int month, int dayOfMonth) {
        FlightSearchPassDataViewModel flightSearchPassDataViewModel = getView().getFlightSearchPassData();
        Calendar calendar = FlightDateUtil.getCurrentCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, dayOfMonth);
        Date dateToSet = calendar.getTime();
        Date twoYears = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);

        if (dateToSet.after(twoYears)) {
            getView().showDepartureDateMaxTwoYears(R.string.flight_dashboard_departure_max_two_years_from_today_error);
        } else if (!getView().isReturning() && dateToSet.before(FlightDateUtil.getCurrentDate())) {
            getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
        } else if (getView().isReturning() && dateToSet.before(FlightDateUtil.stringToDate(flightSearchPassDataViewModel.getDepartureDate()))) {
            getView().showReturnDateShouldGreaterOrEqual(R.string.flight_dashboard_return_should_greater_equal_error);
        } else {
            String dateString = FlightDateUtil.dateToString(dateToSet, FlightDateUtil.DEFAULT_FORMAT);

            if (getView().isReturning()) {
                flightSearchPassDataViewModel.setReturnDate(dateString);
            } else {
                flightSearchPassDataViewModel.setDepartureDate(dateString);
            }
            deleteFlightCache(getView().isReturning());

            getView().setFlightSearchPassData(flightSearchPassDataViewModel);
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

    private Subscriber<FlightSearchWithMetaViewModel> getSubscriberSearchFlightCloud() {
        return new Subscriber<FlightSearchWithMetaViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                getView().showGetListError(e);
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
                getView().showGetListError(e);
            }

            @Override
            public void onNext(List<FlightSearchViewModel> flightSearchViewModels) {
                if (flightSearchViewModels.size() > 0) {
                    getView().showFilterAndSortView();
                    getView().hideSortRouteLoading();
                    getView().clearAdapterData();
                    getView().renderFlightSearchFromCache(flightSearchViewModels);
                    getView().addBottomPaddingForSortAndFilterActionButton();
                    getView().addToolbarElevation();
                } else if (flightSearchViewModels.size() == 0) {
                    getView().removeBottomPaddingForSortAndFilterActionButton();
                    if (getView().isAlreadyFullLoadData()) {
                        getView().clearAdapterData();
                        getView().showEmptyFlightStateView();
                    } else {
                        getView().showSortRouteLoading();
                        getView().hideFilterAndSortView();
                    }
                }
                getView().setSelectedSortItem(sortOptionId);
            }
        };
    }

    public void onSearchItemClicked(FlightSearchViewModel flightSearchViewModel) {
        flightAnalytics.eventSearchProductClick(flightSearchViewModel);
    }

    public void onSeeDetailItemClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        flightAnalytics.eventSearchDetailClick(flightSearchViewModel, adapterPosition);
        flightAnalytics.eventProductDetailImpression(flightSearchViewModel, adapterPosition);
    }

    public void onSearchItemClicked(FlightSearchViewModel flightSearchViewModel, int adapterPosition) {
        flightAnalytics.eventSearchProductClick(flightSearchViewModel, adapterPosition);
    }

    public void initialize() {
        getView().showSortRouteLoading();
        if (!getView().isReturning()) {
            flightAirlineHardRefreshUseCase.execute(RequestParams.EMPTY, new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    getView().showGetListError(e);
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    getView().actionFetchFlightSearchData();
                }
            });
        } else {
            getView().actionFetchFlightSearchData();
        }

    }
}
