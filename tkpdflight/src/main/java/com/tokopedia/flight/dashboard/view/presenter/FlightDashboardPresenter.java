package com.tokopedia.flight.dashboard.view.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.banner.data.source.cloud.model.BannerDetail;
import com.tokopedia.flight.banner.domain.interactor.BannerGetDataUseCase;
import com.tokopedia.flight.common.data.domain.DeleteFlightCacheUseCase;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.domain.GetFlightAirportByIdUseCase;
import com.tokopedia.flight.dashboard.domain.GetFlightClassByIdUseCase;
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase;
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardPassDataViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.mapper.FlightClassViewModelMapper;
import com.tokopedia.flight.dashboard.view.validator.FlightDashboardValidator;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDashboardPresenter extends BaseDaggerPresenter<FlightDashboardContract.View> implements FlightDashboardContract.Presenter {

    private static final String DEVICE_ID = "4";
    private static final String CATEGORY_ID = "27";
    private static final int MAX_PASSENGER_VALUE = 7;
    private static final int MAX_TWO_YEARS = 2;
    private static final int INDEX_DEPARTURE_TRIP = 0;
    private static final int INDEX_RETURN_TRIP = 1;
    private static final int INDEX_ID_AIRPORT_DEPARTURE_TRIP = 0;
    private static final int INDEX_ID_AIRPORT_ARRIVAL_TRIP = 1;
    private static final int INDEX_DATE_TRIP = 2;
    private static final int INDEX_DATE_YEAR = 0;
    private static final int INDEX_DATE_MONTH = 1;
    private static final int INDEX_DATE_DATE = 2;
    private static final int DEFAULT_ADULT_PASSENGER = 1;
    private static final int DEFAULT_CHILD_PASSENGER = 0;
    private static final int DEFAULT_INFANT_PASSENGER = 0;

    private BannerGetDataUseCase bannerGetDataUseCase;
    private FlightDashboardValidator validator;
    private DeleteFlightCacheUseCase deleteFlightCacheUseCase;
    private GetFlightClassesUseCase getFlightClassesUseCase;
    private GetFlightAirportByIdUseCase getFlightAirportByIdUseCase;
    private GetFlightClassByIdUseCase getFlightClassByIdUseCase;
    private FlightClassViewModelMapper flightClassViewModelMapper;
    private FlightDashboardCache flightDashboardCache;
    private UserSession userSession;
    private FlightAnalytics flightAnalytics;
    private CompositeSubscription compositeSubscription;

    @Inject
    public FlightDashboardPresenter(BannerGetDataUseCase bannerGetDataUseCase,
                                    FlightDashboardValidator validator,
                                    DeleteFlightCacheUseCase deleteFlightCacheUseCase,
                                    GetFlightClassesUseCase getFlightClassesUseCase,
                                    GetFlightAirportByIdUseCase getFlightAirportByIdUseCase,
                                    GetFlightClassByIdUseCase getFlightClassByIdUseCase,
                                    FlightClassViewModelMapper flightClassViewModelMapper,
                                    FlightDashboardCache flightDashboardCache,
                                    UserSession userSession,
                                    FlightAnalytics flightAnalytics) {
        this.bannerGetDataUseCase = bannerGetDataUseCase;
        this.validator = validator;
        this.deleteFlightCacheUseCase = deleteFlightCacheUseCase;
        this.getFlightClassesUseCase = getFlightClassesUseCase;
        this.getFlightAirportByIdUseCase = getFlightAirportByIdUseCase;
        this.getFlightClassByIdUseCase = getFlightClassByIdUseCase;
        this.flightClassViewModelMapper = flightClassViewModelMapper;
        this.flightDashboardCache = flightDashboardCache;
        this.userSession = userSession;
        this.flightAnalytics = flightAnalytics;
        this.compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onSingleTripChecked() {
        flightAnalytics.eventTripTypeClick(getView().getString(R.string.flight_dashboard_analytic_one_way).toString());
        getView().getCurrentDashboardViewModel().setOneWay(true);
        getView().renderSingleTripView();
    }

    @Override
    public void onRoundTripChecked() {
        flightAnalytics.eventTripTypeClick(getView().getString(R.string.flight_dashboard_analytic_round_trip).toString());
        getView().getCurrentDashboardViewModel().setOneWay(false);
        getView().renderRoundTripView();
    }

    @Override
    public void initialize() {
        if (userSession.isLoggedIn()) {
            onInitialize();
        } else {
            getView().navigateToLoginPage();
        }
    }

    private void onInitialize() {
        setupViewModel();
        getBannerData();

        if (!getView().isFromApplink()) {
            actionLoadFromCache();
        } else {
            transformExtras();
        }
    }

    private void actionLoadFromCache() {
        FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();
        flightDashboardPassDataViewModel.setDepartureAirportId(flightDashboardCache.getDepartureAirport());
        flightDashboardPassDataViewModel.setArrivalAirportId(flightDashboardCache.getArrivalAirport());
        flightDashboardPassDataViewModel.setDepartureDate(flightDashboardCache.getDepartureDate());
        flightDashboardPassDataViewModel.setReturnDate(flightDashboardCache.getReturnDate());
        flightDashboardPassDataViewModel.setAdultPassengerCount(flightDashboardCache.getPassengerAdult());
        flightDashboardPassDataViewModel.setChildPassengerCount(flightDashboardCache.getPassengerChild());
        flightDashboardPassDataViewModel.setInfantPassengerCount(flightDashboardCache.getPassengerInfant());
        flightDashboardPassDataViewModel.setFlightClass(flightDashboardCache.getClassCache());

        actionRenderFromPassData(false);
    }

    private void setupViewModel() {
        Date currentDate = FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        Date returnDate = FlightDateUtil.addTimeToCurrentDate(Calendar.DATE, 2);
        String departureDateString = FlightDateUtil.dateToString(currentDate, FlightDateUtil.DEFAULT_FORMAT);
        String departureDateFmtString = FlightDateUtil.dateToString(currentDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        String returnDateString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_FORMAT);
        String returnDateFmtString = FlightDateUtil.dateToString(returnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        FlightPassengerViewModel passData = new FlightPassengerViewModel.Builder()
                .setAdult(1)
                .build();
        String passengerFmt = buildPassengerTextFormatted(passData);
        FlightDashboardViewModel viewModel = new FlightDashboardViewModel.Builder()
                .setFlightPassengerViewModel(passData)
                .setIsOneWay(true)
                .setDepartureDate(departureDateString)
                .setReturnDate(returnDateString)
                .setDepartureDateFmt(departureDateFmtString)
                .setReturnDateFmt(returnDateFmtString)
                .setFlightPassengerFmt(passengerFmt)
                .setDepartureAirportFmt("")
                .setArrivalAirportFmt("")
                .build();

        getView().setDashBoardViewModel(viewModel);
    }

    @NonNull
    private String buildPassengerTextFormatted(FlightPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getView().getString(R.string.flight_dashboard_adult_passenger);
            if (passData.getChildren() > 0) {
                passengerFmt += ", " + passData.getChildren() + " " + getView().getString(R.string.flight_dashboard_adult_children);
            }
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getView().getString(R.string.flight_dashboard_adult_infant);
            }
        }
        return passengerFmt;
    }

    @Override
    public void onReverseAirportButtonClicked() {
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        FlightAirportDB flightAirportDB = viewModel.getArrivalAirport();
        String destinationFmt = viewModel.getArrivalAirportFmt();
        viewModel.setArrivalAirport(viewModel.getDepartureAirport());
        viewModel.setArrivalAirportFmt(viewModel.getDepartureAirportFmt());
        viewModel.setDepartureAirport(flightAirportDB);
        viewModel.setDepartureAirportFmt(destinationFmt);
        getView().setDashBoardViewModel(viewModel);
        renderUi();
    }

    @Override
    public void onDepartureDateButtonClicked() {
        Date minDate = FlightDateUtil.getCurrentDate();
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
        Date selectedDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getDepartureDate());
        getView().showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onDepartureDateChange(int year, int month, int dayOfMonth) {
        flightDashboardCache.putDepartureDate(year + "-" + month + "-" + dayOfMonth);
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);
        Date newDepartureDate = now.getTime();
        String newDepartureDateStr = FlightDateUtil.dateToString(newDepartureDate, FlightDateUtil.DEFAULT_FORMAT);
        viewModel.setDepartureDate(newDepartureDateStr);
        String newDepartureDateFmtStr = FlightDateUtil.dateToString(newDepartureDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        viewModel.setDepartureDateFmt(newDepartureDateFmtStr);
        if (!viewModel.isOneWay()) {
            Date currentReturnDate = FlightDateUtil.stringToDate(viewModel.getReturnDate());
            if (currentReturnDate.compareTo(newDepartureDate) < 0) {
                Date reAssignReturnDate = FlightDateUtil.addDate(newDepartureDate, 1);
                viewModel.setReturnDate(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_FORMAT));
                viewModel.setReturnDateFmt(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
            }
            getView().setDashBoardViewModel(viewModel);
            getView().renderRoundTripView();
        } else {
            Date reAssignReturnDate = FlightDateUtil.addDate(newDepartureDate, 1);
            viewModel.setReturnDate(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_FORMAT));
            viewModel.setReturnDateFmt(FlightDateUtil.dateToString(reAssignReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT));
            getView().setDashBoardViewModel(viewModel);
            getView().renderSingleTripView();
        }
    }

    @Override
    public void onReturnDateButtonClicked() {
        Date selectedDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getReturnDate());
        Date minDate = FlightDateUtil.stringToDate(getView().getCurrentDashboardViewModel().getDepartureDate());
        Date maxDate = FlightDateUtil.addTimeToCurrentDate(Calendar.YEAR, 2);
        getView().showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onReturnDateChange(int year, int month, int dayOfMonth) {
        FlightDashboardViewModel viewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        Calendar now = FlightDateUtil.getCurrentCalendar();
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, dayOfMonth);
        Date newReturnDate = now.getTime();
        String newReturnDateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_FORMAT);
        viewModel.setReturnDate(newReturnDateStr);
        String newReturnDateFmtStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        viewModel.setReturnDateFmt(newReturnDateFmtStr);
        getView().setDashBoardViewModel(viewModel);
        renderUi();
    }

    private void renderUi() {
        if (!getView().getCurrentDashboardViewModel().isOneWay()) {
            getView().renderRoundTripView();
        } else {
            getView().renderSingleTripView();
        }
    }

    @Override
    public void onFlightClassesChange(FlightClassViewModel viewModel) {
        flightDashboardCache.putClassCache(viewModel.getId());
        flightAnalytics.eventClassClick(viewModel.getTitle());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightClass(viewModel);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onFlightPassengerChange(FlightPassengerViewModel passengerViewModel) {
        flightDashboardCache.putPassengerCount(passengerViewModel.getAdult(), passengerViewModel.getChildren(), passengerViewModel.getInfant());
        flightAnalytics.eventPassengerClick(passengerViewModel.getAdult(), passengerViewModel.getChildren(), passengerViewModel.getInfant());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightPassengerViewModel(passengerViewModel);
        flightDashboardViewModel.setFlightPassengerFmt(buildPassengerTextFormatted(passengerViewModel));
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onDepartureAirportChange(FlightAirportDB departureAirport) {
        flightAnalytics.eventOriginClick(departureAirport.getCityName(), departureAirport.getAirportId());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setDepartureAirport(departureAirport);
        String code = buildAirportFmt(departureAirport);
        flightDashboardViewModel.setDepartureAirportFmt(code);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        flightDashboardCache.putDepartureAirport(departureAirport.getAirportId());
        renderUi();
    }

    @NonNull
    private String buildAirportFmt(FlightAirportDB departureAirport) {
        String code = departureAirport.getAirportId();
        if (TextUtils.isEmpty(code)) {
            code = departureAirport.getCityCode();
        }
        code = departureAirport.getCityName() + " (" + code + ")";
        return code;
    }

    @Override
    public void onArrivalAirportChange(FlightAirportDB arrivalAirport) {
        flightAnalytics.eventDestinationClick(arrivalAirport.getCityName(), arrivalAirport.getAirportId());
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setArrivalAirport(arrivalAirport);
        String code = arrivalAirport.getAirportId();
        if (TextUtils.isEmpty(code)) {
            code = arrivalAirport.getCityCode();
        }
        flightDashboardViewModel.setArrivalAirportFmt(arrivalAirport.getCityName() + " (" + code + ")");
        getView().setDashBoardViewModel(flightDashboardViewModel);
        flightDashboardCache.putArrivalAirport(arrivalAirport.getAirportId());
        renderUi();
    }

    @Override
    public void onSearchTicketButtonClicked() {
        if (validateSearchParam(getView().getCurrentDashboardViewModel())) {
            flightAnalytics.eventSearchClick(getView().getScreenName());
            deleteFlightCacheUseCase.execute(DeleteFlightCacheUseCase.createRequestParam(), new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if (isViewAttached()) {
                        getView().navigateToSearchPage(getView().getCurrentDashboardViewModel());
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe();
        detachView();
    }

    @Override
    public void onLoginResultReceived() {
        if (userSession.isLoggedIn()) {
            onInitialize();
        } else {
            getView().closePage();
        }
    }

    private void transformExtras() {
        try {
            FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();

            boolean isDepartureDateValid = true;
            boolean isReturnDateValid = true;
            boolean isPassengerValid = true;

            // transform trip extras
            String[] tempExtras = getView().getTripArguments().split(",");
            String[] extrasTripDeparture = tempExtras[INDEX_DEPARTURE_TRIP].split("_");
            String[] departureTripDate = extrasTripDeparture[INDEX_DATE_TRIP].split("-");

            /**
             * tokopedia://flight/search?dest=CGK_DPS_2018-04-01,CGK_DPS_2018-05-01&a=1&c=1&i=1&s=1
             */

            flightDashboardPassDataViewModel.setDepartureAirportId(extrasTripDeparture[INDEX_ID_AIRPORT_DEPARTURE_TRIP]);
            flightDashboardPassDataViewModel.setArrivalAirportId(extrasTripDeparture[INDEX_ID_AIRPORT_ARRIVAL_TRIP]);

            Calendar today = FlightDateUtil.getCurrentCalendar();
            if (!validator.validateDepartureDateAtLeastToday(getView().getCurrentDashboardViewModel())) {
                isDepartureDateValid = false;
                getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
                flightDashboardPassDataViewModel.setDepartureDate(FlightDateUtil.dateToString(today.getTime(), FlightDateUtil.DEFAULT_FORMAT));
            } else if ((Integer.parseInt(departureTripDate[INDEX_DATE_YEAR]) - today.get(Calendar.YEAR)) > MAX_TWO_YEARS) {
                isDepartureDateValid = false;
                getView().showApplinkErrorMessage(R.string.flight_dashboard_departure_max_two_years_from_today_error);
                flightDashboardPassDataViewModel.setDepartureDate(FlightDateUtil.dateToString(today.getTime(), FlightDateUtil.DEFAULT_FORMAT));
            } else {
                flightDashboardPassDataViewModel.setDepartureDate(extrasTripDeparture[INDEX_DATE_TRIP]);
            }

            if (tempExtras.length > 1) {
                String[] extrasTripReturn = tempExtras[INDEX_RETURN_TRIP].split("_");
                String[] returnTripDate = extrasTripReturn[INDEX_DATE_TRIP].split("-");

                if (!validator.validateArrivalDateShouldGreaterOrEqualDeparture(getView().getCurrentDashboardViewModel())) {
                    isReturnDateValid = false;
                    if (isDepartureDateValid) {
                        getView().showArrivalDateShouldGreaterOrEqual(R.string.flight_dashboard_arrival_should_greater_equal_error);
                        flightDashboardPassDataViewModel.setReturnDate(departureTripDate[INDEX_DATE_YEAR] + "-" + departureTripDate[INDEX_DATE_MONTH] + "-" + Integer.parseInt(departureTripDate[INDEX_DATE_DATE]) + 1);
                    } else {
                        today.add(Calendar.DATE, +1);
                        flightDashboardPassDataViewModel.setReturnDate(FlightDateUtil.dateToString(today.getTime(), FlightDateUtil.DEFAULT_FORMAT));
                    }
                } else if ((Integer.parseInt(returnTripDate[INDEX_DATE_YEAR]) - today.get(Calendar.YEAR)) > MAX_TWO_YEARS) {
                    isReturnDateValid = false;
                    if (isDepartureDateValid) {
                        getView().showApplinkErrorMessage(R.string.flight_dashboard_arrival_max_two_years_from_today_error);
                        flightDashboardPassDataViewModel.setReturnDate(departureTripDate[INDEX_DATE_YEAR] + "-" + departureTripDate[INDEX_DATE_MONTH] + "-" + Integer.parseInt(departureTripDate[INDEX_DATE_DATE]) + 1);
                    } else {
                        today.add(Calendar.DATE, +1);
                        flightDashboardPassDataViewModel.setReturnDate(FlightDateUtil.dateToString(today.getTime(), FlightDateUtil.DEFAULT_FORMAT));
                    }
                }
            } else {
                flightDashboardPassDataViewModel.setReturnDate("");
            }

            // transform passenger count
            if (Integer.parseInt(getView().getChildPassengerArguments()) > Integer.parseInt(getView().getAdultPassengerArguments()) || Integer.parseInt(getView().getInfantPassengerArguments()) > Integer.parseInt(getView().getAdultPassengerArguments())) {
                isPassengerValid = false;
                getView().showApplinkErrorMessage(R.string.select_passenger_infant_greater_than_adult_error_message);
                flightDashboardPassDataViewModel.setAdultPassengerCount(DEFAULT_ADULT_PASSENGER);
                flightDashboardPassDataViewModel.setChildPassengerCount(DEFAULT_CHILD_PASSENGER);
                flightDashboardPassDataViewModel.setInfantPassengerCount(DEFAULT_INFANT_PASSENGER);
            } else if (Integer.parseInt(getView().getChildPassengerArguments()) + Integer.parseInt(getView().getAdultPassengerArguments()) > MAX_PASSENGER_VALUE) {
                isPassengerValid = false;
                getView().showApplinkErrorMessage(R.string.select_passenger_total_passenger_error_message);
                flightDashboardPassDataViewModel.setAdultPassengerCount(DEFAULT_ADULT_PASSENGER);
                flightDashboardPassDataViewModel.setChildPassengerCount(DEFAULT_CHILD_PASSENGER);
                flightDashboardPassDataViewModel.setInfantPassengerCount(DEFAULT_INFANT_PASSENGER);
            } else {
                flightDashboardPassDataViewModel.setAdultPassengerCount(Integer.parseInt(getView().getAdultPassengerArguments()));
                flightDashboardPassDataViewModel.setChildPassengerCount(Integer.parseInt(getView().getChildPassengerArguments()));
                flightDashboardPassDataViewModel.setInfantPassengerCount(Integer.parseInt(getView().getInfantPassengerArguments()));
            }

            // transform class
            int classId = Integer.parseInt(getView().getClassArguments());
            flightDashboardPassDataViewModel.setFlightClass(classId);

            getView().setDashboardPassData(flightDashboardPassDataViewModel);
            actionRenderFromPassData(isDepartureDateValid && isReturnDateValid && isPassengerValid);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void actionRenderFromPassData(final boolean isSearchImmediately) {
        FlightDashboardPassDataViewModel flightDashboardPassDataViewModel = getView().getDashboardPassData();
        String[] departureDate = flightDashboardPassDataViewModel.getDepartureDate().split("-");
        onDepartureDateChange(Integer.parseInt(departureDate[INDEX_DATE_YEAR]), Integer.parseInt(departureDate[INDEX_DATE_MONTH]), Integer.parseInt(departureDate[INDEX_DATE_DATE]));
        onSingleTripChecked();

        if (!flightDashboardPassDataViewModel.getReturnDate().isEmpty()) {
            String[] returnDate = flightDashboardPassDataViewModel.getReturnDate().split("-");
            onReturnDateChange(Integer.parseInt(returnDate[INDEX_DATE_YEAR]), Integer.parseInt(returnDate[INDEX_DATE_MONTH]), Integer.parseInt(returnDate[INDEX_DATE_DATE]));
            onRoundTripChecked();
        }

        onFlightPassengerChange(new FlightPassengerViewModel(flightDashboardPassDataViewModel.getAdultPassengerCount(), flightDashboardPassDataViewModel.getChildPassengerCount(), flightDashboardPassDataViewModel.getInfantPassengerCount()));

        compositeSubscription.add(
                Observable.zip(getFlightAirportByIdUseCase
                                .createObservable(getFlightAirportByIdUseCase.createRequestParams(flightDashboardPassDataViewModel.getDepartureAirportId())),
                        getFlightAirportByIdUseCase
                                .createObservable(getFlightAirportByIdUseCase.createRequestParams(flightDashboardPassDataViewModel.getArrivalAirportId())),
                        getFlightClassByIdUseCase.createObservable(getFlightClassByIdUseCase.createRequestParams(flightDashboardPassDataViewModel.getFlightClass())),
                        new Func3<FlightAirportDB, FlightAirportDB, FlightClassEntity, Boolean>() {
                            @Override
                            public Boolean call(FlightAirportDB flightAirportDB, FlightAirportDB flightAirportDB2, FlightClassEntity flightClassEntity) {
                                onDepartureAirportChange(flightAirportDB);
                                onArrivalAirportChange(flightAirportDB2);
                                onFlightClassesChange(flightClassViewModelMapper.transform(flightClassEntity));
                                return true;
                            }
                        }
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (isSearchImmediately)
                            onSearchTicketButtonClicked();
                    }
                })
        );
    }

    @Override
    public void onBannerItemClick(int position, BannerDetail bannerDetail) {
        flightAnalytics.eventPromotionClick(position + 1, bannerDetail.getAttributes().getTitle(), bannerDetail.getAttributes().getImgUrl());
    }

    private void getBannerData() {
        bannerGetDataUseCase.execute(bannerGetDataUseCase.createRequestParams(DEVICE_ID, CATEGORY_ID), new Subscriber<List<BannerDetail>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if (isViewAttached()) {
                    getView().hideBannerView();
                }
            }

            @Override
            public void onNext(List<BannerDetail> bannerDetailList) {
                if (isViewAttached()) {
                    getView().renderBannerView(bannerDetailList);
                }
            }
        });
    }

    private boolean validateSearchParam(FlightDashboardViewModel currentDashboardViewModel) {
        boolean isValid = true;
        if (!validator.validateDepartureNotEmtpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showDepartureEmptyErrorMessage(R.string.flight_dashboard_departure_empty_error);
        } else if (!validator.validateArrivalNotEmpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showArrivalEmptyErrorMessage(R.string.flight_dashboard_arrival_empty_error);
        } else if (!validator.validateArrivalAndDestinationNotSame(currentDashboardViewModel)) {
            isValid = false;
            getView().showArrivalAndDestinationAreSameError(R.string.flight_dashboard_arrival_departure_same_error);
        } else if (!validator.validateDepartureDateAtLeastToday(currentDashboardViewModel)) {
            isValid = false;
            getView().showDepartureDateShouldAtLeastToday(R.string.flight_dashboard_departure_should_atleast_today_error);
        } else if (!validator.validateAirportsShouldDifferentCity(currentDashboardViewModel)) {
            isValid = false;
            getView()
                    .showAirportShouldDifferentCity(R.string.flight_dashboard_departure_should_different_city_error);
        } else if (!validator.validateArrivalDateShouldGreaterOrEqualDeparture(currentDashboardViewModel)) {
            isValid = false;
            getView().showArrivalDateShouldGreaterOrEqual(R.string.flight_dashboard_arrival_should_greater_equal_error);
        } else if (!validator.validatePassengerAtLeastOneAdult(currentDashboardViewModel)) {
            isValid = false;
            getView().showPassengerAtLeastOneAdult(R.string.flight_dashboard_at_least_one_adult_error);
        } else if (!validator.validateFlightClassNotEmpty(currentDashboardViewModel)) {
            isValid = false;
            getView().showFlightClassPassengerIsEmpty(R.string.flight_dashboard_fligh_class_is_empty);
        }
        return isValid;
    }

    @Nullable
    private FlightDashboardViewModel cloneViewModel(FlightDashboardViewModel currentDashboardViewModel) {
        FlightDashboardViewModel viewModel = null;
        try {
            viewModel = (FlightDashboardViewModel) currentDashboardViewModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to Clone FlightDashboardViewModel");
        }
        return viewModel;
    }

}
