package com.tokopedia.flight.search.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.data.cloud.model.response.Route;
import com.tokopedia.flight.search.view.FlightSearchReturnView;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author by alvarisi on 1/10/18.
 */

public class FlightSearchReturnPresenter extends BaseDaggerPresenter<FlightSearchReturnView> {
    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightSearchReturnPresenter(FlightBookingGetSingleResultUseCase flightBookingGetSingleResultUseCase) {
        this.flightBookingGetSingleResultUseCase = flightBookingGetSingleResultUseCase;
        this.compositeSubscription = new CompositeSubscription();
    }

    public void onFlightSearchSelected(String selectedFlightDeparture,
                                       final FlightSearchViewModel returnFlightSearchViewModel) {
        flightBookingGetSingleResultUseCase.execute(flightBookingGetSingleResultUseCase.createRequestParam(false, selectedFlightDeparture),
                new Subscriber<FlightSearchViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().showErrorPickJourney();
                        }
                    }

                    @Override
                    public void onNext(FlightSearchViewModel departureFlightSearchViewModel) {
                        if (isValidReturnJourney(departureFlightSearchViewModel, returnFlightSearchViewModel)) {
                            getView().navigateToCart(returnFlightSearchViewModel);
                        } else {
                            getView().showReturnTimeShouldGreaterThanArrivalDeparture();
                        }
                    }
                });
    }

    private boolean isValidReturnJourney(FlightSearchViewModel departureViewModel, FlightSearchViewModel returnViewModel) {
        if (departureViewModel.getRouteList() != null && returnViewModel.getRouteList() != null) {
            if (departureViewModel.getRouteList().size() > 0 && returnViewModel.getRouteList().size() > 0) {
                Route lastDepartureRoute = departureViewModel.getRouteList().get(departureViewModel.getRouteList().size() - 1);
                Route firstReturnRoute = returnViewModel.getRouteList().get(0);
                Date departureArrivalTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, lastDepartureRoute.getArrivalTimestamp());
                Date returnDepartureTime = FlightDateUtil.stringToDate(FlightDateUtil.FORMAT_DATE_API, firstReturnRoute.getDepartureTimestamp());
                long different = returnDepartureTime.getTime() - departureArrivalTime.getTime();
                if (different >= 0) {
                    long hours = different / ONE_HOUR;
                    CommonUtils.dumper("diff : " + hours);
                    return hours >= 6;
                } else {
                    return false;
                }

            }
        }
        return true;
    }

    public void onFlightSearchSelected(String selectedFlightDeparture, final String selectedFlightReturn) {
        compositeSubscription.add(Observable.zip(
                flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, selectedFlightDeparture)),
                flightBookingGetSingleResultUseCase.createObservable(flightBookingGetSingleResultUseCase.createRequestParam(true, selectedFlightReturn)),
                new Func2<FlightSearchViewModel, FlightSearchViewModel, Boolean>() {
                    @Override
                    public Boolean call(FlightSearchViewModel departureViewModel, FlightSearchViewModel returnViewModel) {
                        return isValidReturnJourney(departureViewModel, returnViewModel);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (isViewAttached()) {
                            getView().showErrorPickJourney();
                        }
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            getView().navigateToCart(selectedFlightReturn);
                        } else {
                            getView().showReturnTimeShouldGreaterThanArrivalDeparture();
                        }
                    }
                })
        );
    }

    public void onDestroy() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }
}
