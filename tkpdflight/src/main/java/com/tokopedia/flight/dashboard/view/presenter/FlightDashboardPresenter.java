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
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase;
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.mapper.FlightClassViewModelMapper;
import com.tokopedia.flight.dashboard.view.validator.FlightDashboardValidator;
import com.tokopedia.usecase.RequestParams;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightDashboardPresenter extends BaseDaggerPresenter<FlightDashboardContract.View> implements FlightDashboardContract.Presenter {

    private static final String DEVICE_ID = "4";
    private static final String CATEGORY_ID = "27";

    private BannerGetDataUseCase bannerGetDataUseCase;
    private FlightDashboardValidator validator;
    private DeleteFlightCacheUseCase deleteFlightCacheUseCase;
    private GetFlightClassesUseCase getFlightClassesUseCase;
    private FlightClassViewModelMapper flightClassViewModelMapper;
    private FlightDashboardCache flightDashboardCache;
    private UserSession userSession;

    @Inject
    public FlightDashboardPresenter(BannerGetDataUseCase bannerGetDataUseCase,
                                    FlightDashboardValidator validator,
                                    DeleteFlightCacheUseCase deleteFlightCacheUseCase,
                                    GetFlightClassesUseCase getFlightClassesUseCase,
                                    FlightClassViewModelMapper flightClassViewModelMapper,
                                    FlightDashboardCache flightDashboardCache,
                                    UserSession userSession) {
        this.bannerGetDataUseCase = bannerGetDataUseCase;
        this.validator = validator;
        this.deleteFlightCacheUseCase = deleteFlightCacheUseCase;
        this.getFlightClassesUseCase = getFlightClassesUseCase;
        this.flightClassViewModelMapper = flightClassViewModelMapper;
        this.flightDashboardCache = flightDashboardCache;
        this.userSession = userSession;
    }

    @Override
    public void onSingleTripChecked() {
        getView().getCurrentDashboardViewModel().setOneWay(true);
        getView().renderSingleTripView();
    }

    @Override
    public void onRoundTripChecked() {
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
        actionLoadFromCache();
        actionGetClassesAndSetDefaultClass();
        getBannerData();
    }

    private void actionLoadFromCache() {
        boolean isChanged = false;
        if (flightDashboardCache.getDepartureAirport() != null) {
            getView().getCurrentDashboardViewModel().setDepartureAirport(flightDashboardCache.getDepartureAirport());
            getView().getCurrentDashboardViewModel().setDepartureAirportFmt(buildAirportFmt(flightDashboardCache.getDepartureAirport()));
            isChanged = true;
        }
        if (flightDashboardCache.getArrivalAirport() != null) {
            getView().getCurrentDashboardViewModel().setArrivalAirport(flightDashboardCache.getArrivalAirport());
            getView().getCurrentDashboardViewModel().setArrivalAirportFmt(buildAirportFmt(flightDashboardCache.getArrivalAirport()));
            isChanged = true;
        }
        if (isChanged) {
            renderUi();
        }
    }

    private void actionGetClassesAndSetDefaultClass() {
        getFlightClassesUseCase.execute(RequestParams.EMPTY, new Subscriber<List<FlightClassEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onNext(List<FlightClassEntity> entities) {
                if (isViewAttached()) {
                    if (entities != null && entities.size() > 0) {
                        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
                        flightDashboardViewModel.setFlightClass(flightClassViewModelMapper.transform(entities.get(0)));
                        getView().setDashBoardViewModel(flightDashboardViewModel);
                        renderUi();
                    }
                }
            }
        });
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
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightClass(viewModel);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onFlightPassengerChange(FlightPassengerViewModel passengerViewModel) {
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setFlightPassengerViewModel(passengerViewModel);
        flightDashboardViewModel.setFlightPassengerFmt(buildPassengerTextFormatted(passengerViewModel));
        getView().setDashBoardViewModel(flightDashboardViewModel);
        renderUi();
    }

    @Override
    public void onDepartureAirportChange(FlightAirportDB departureAirport) {
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setDepartureAirport(departureAirport);
        String code = buildAirportFmt(departureAirport);
        flightDashboardViewModel.setDepartureAirportFmt(code);
        getView().setDashBoardViewModel(flightDashboardViewModel);
        flightDashboardCache.putDepartureAirport(departureAirport);
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
        FlightDashboardViewModel flightDashboardViewModel = cloneViewModel(getView().getCurrentDashboardViewModel());
        flightDashboardViewModel.setArrivalAirport(arrivalAirport);
        String code = arrivalAirport.getAirportId();
        if (TextUtils.isEmpty(code)) {
            code = arrivalAirport.getCityCode();
        }
        flightDashboardViewModel.setArrivalAirportFmt(arrivalAirport.getCityName() + " (" + code + ")");
        getView().setDashBoardViewModel(flightDashboardViewModel);
        flightDashboardCache.putArrivalAirport(arrivalAirport);
        renderUi();
    }

    @Override
    public void onSearchTicketButtonClicked() {
        if (validateSearchParam(getView().getCurrentDashboardViewModel())) {
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

    private void getBannerData() {
        bannerGetDataUseCase.execute(bannerGetDataUseCase.createRequestParams(DEVICE_ID, CATEGORY_ID), new Subscriber<List<BannerDetail>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                if(isViewAttached()) {
                    getView().hideBannerView();
                }
            }

            @Override
            public void onNext(List<BannerDetail> bannerDetailList) {
                if(isViewAttached()) {
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
