package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

/**
 * @author by alvarisi on 11/16/17.
 *         TODO :this class still
 */

public class FlightBookingPassengerPresenter extends BaseDaggerPresenter<FlightBookingPassengerContract.View> implements FlightBookingPassengerContract.Presenter {


    @Inject
    public FlightBookingPassengerPresenter() {

    }


    @Override
    public void onViewCreated() {
        getView().renderHeaderTitle(getView().getCurrentPassengerViewModel().getHeaderTitle());

        if (isAdultPassenger()) {
            getView().renderHeaderSubtitle(R.string.flight_booking_passenger_adult_subtitle);
            getView().hideBirthdayInputView();
            getView().renderSpinnerForAdult();
        } else {
            getView().renderSpinnerForChildAndInfant();
            getView().showBirthdayInputView();
            if (isChildPassenger()) {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_child_subtitle);
            } else {
                getView().renderHeaderSubtitle(R.string.flight_booking_passenger_infant_subtitle);
            }
        }

        if (isAdultPassenger() || isChildPassenger()) {
            getView().renderPassengerLuggages(getView().getLuggageViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels());
            getView().renderPassengerMeals(getView().getMealViewModels(), getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels());
            /*if (isRoundTrip()) {
                flightBookingGetSingleResultUseCase
                        .createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureId()))
                        .zipWith(flightBookingGetSingleResultUseCase.createObservable(
                                flightBookingGetSingleResultUseCase.createRequestParam(true, getView().getReturnTripId())),
                                new Func2<FlightSearchViewModel, FlightSearchViewModel, Boolean>() {
                                    @Override
                                    public Boolean call(FlightSearchViewModel departureViewModel, FlightSearchViewModel returnViewModel) {
                                        if (isViewAttached()) {
                                            if (getView().getMealViewModels() != null && getView().getMealViewModels().size() > 0) {
                                                List<FlightBookingMealRouteViewModel> flightBookingMealRouteViewModels = new ArrayList<>();
                                                for (Route route : departureViewModel.getRouteList()) {
                                                    List<FlightBookingMealViewModel> flightBookingMealViewModels = new ArrayList<>();
                                                    for (FlightBookingMealViewModel mealViewModel : getView().getMealViewModels()) {
                                                        if (mealViewModel.getId().contains(route.getDepartureAirport()) &&
                                                                mealViewModel.getId().contains(route.getArrivalAirport())) {
                                                            flightBookingMealViewModels.add(mealViewModel);
                                                        }
                                                    }

                                                    if (flightBookingMealViewModels.size() > 0) {
                                                        FlightBookingMealRouteViewModel flightBookingMealRouteViewModel = new FlightBookingMealRouteViewModel();
                                                        flightBookingMealRouteViewModel.setRoute(route);
                                                        flightBookingMealRouteViewModel.setMealViewModels(flightBookingMealViewModels);
                                                        flightBookingMealRouteViewModels.add(flightBookingMealRouteViewModel);
                                                    }
                                                }
                                                for (Route route : returnViewModel.getRouteList()) {
                                                    List<FlightBookingMealViewModel> flightBookingMealViewModels = new ArrayList<>();
                                                    for (FlightBookingMealViewModel mealViewModel : getView().getMealViewModels()) {
                                                        if (mealViewModel.getId().contains(route.getDepartureAirport()) &&
                                                                mealViewModel.getId().contains(route.getArrivalAirport())) {
                                                            flightBookingMealViewModels.add(mealViewModel);
                                                        }
                                                        if (flightBookingMealRouteViewModels.size() > 0) {
                                                            FlightBookingMealRouteViewModel flightBookingMealRouteViewModel = new FlightBookingMealRouteViewModel();
                                                            flightBookingMealRouteViewModel.setRoute(route);
                                                            flightBookingMealRouteViewModel.setMealViewModels(flightBookingMealViewModels);
                                                            flightBookingMealRouteViewModels.add(flightBookingMealRouteViewModel);
                                                        }
                                                    }
                                                }
                                                if (flightBookingMealRouteViewModels.size() > 0) {
                                                    getView().renderPassengerMeals(flightBookingMealRouteViewModels,
                                                            getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels());
                                                }
                                            }

                                            if (getView().getLuggageViewModels() != null && getView().getLuggageViewModels().size() > 0) {
                                                List<FlightBookingLuggageRouteViewModel> flightBookingLuggageRouteViewModels = new ArrayList<>();
                                                for (Route route : departureViewModel.getRouteList()) {
                                                    List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels = new ArrayList<>();
                                                    for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : getView().getLuggageViewModels()) {
                                                        if (flightBookingLuggageViewModel.getId().contains(route.getArrivalAirport()) &&
                                                                flightBookingLuggageViewModel.getId().contains(route.getDepartureAirport())) {
                                                            flightBookingLuggageViewModels.add(flightBookingLuggageViewModel);
                                                        }
                                                    }
                                                    if (flightBookingLuggageViewModels.size() > 0) {
                                                        FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel = new FlightBookingLuggageRouteViewModel();
                                                        flightBookingLuggageRouteViewModel.setRoute(route);
                                                        flightBookingLuggageRouteViewModel.setLuggage(flightBookingLuggageViewModels);
                                                        flightBookingLuggageRouteViewModels.add(flightBookingLuggageRouteViewModel);
                                                    }
                                                }
                                                for (Route route : returnViewModel.getRouteList()) {
                                                    List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels = new ArrayList<>();
                                                    for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : getView().getLuggageViewModels()) {
                                                        if (flightBookingLuggageViewModel.getId().contains(route.getArrivalAirport()) &&
                                                                flightBookingLuggageViewModel.getId().contains(route.getDepartureAirport())) {
                                                            flightBookingLuggageViewModels.add(flightBookingLuggageViewModel);
                                                        }
                                                    }
                                                    if (flightBookingLuggageViewModels.size() > 0) {
                                                        FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel = new FlightBookingLuggageRouteViewModel();
                                                        flightBookingLuggageRouteViewModel.setRoute(route);
                                                        flightBookingLuggageRouteViewModel.setLuggage(flightBookingLuggageViewModels);
                                                        flightBookingLuggageRouteViewModels.add(flightBookingLuggageRouteViewModel);
                                                    }
                                                }

                                                if (flightBookingLuggageRouteViewModels.size() > 0) {
                                                    getView().renderPassengerLuggages(flightBookingLuggageRouteViewModels,
                                                            getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels());
                                                }
                                            }
                                        }
                                        return true;
                                    }
                                })
                        .onBackpressureDrop()
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe();
            } else {
                flightBookingGetSingleResultUseCase
                        .createObservable(flightBookingGetSingleResultUseCase.createRequestParam(false, getView().getDepartureId()))
                        .onBackpressureDrop()
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<FlightSearchViewModel>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                                if (isViewAttached()) {

                                }
                            }

                            @Override
                            public void onNext(FlightSearchViewModel departureViewModel) {
                                if (isViewAttached()) {
                                    if (getView().getMealViewModels() != null && getView().getMealViewModels().size() > 0) {
                                        List<FlightBookingMealRouteViewModel> flightBookingMealRouteViewModels = new ArrayList<>();
                                        for (Route route : departureViewModel.getRouteList()) {
                                            List<FlightBookingMealViewModel> flightBookingMealViewModels = new ArrayList<>();
                                            for (FlightBookingMealViewModel mealViewModel : getView().getMealViewModels()) {
                                                if (mealViewModel.getId().contains(route.getDepartureAirport()) &&
                                                        mealViewModel.getId().contains(route.getArrivalAirport())) {
                                                    flightBookingMealViewModels.add(mealViewModel);
                                                }
                                            }

                                            if (flightBookingMealViewModels.size() > 0) {
                                                FlightBookingMealRouteViewModel flightBookingMealRouteViewModel = new FlightBookingMealRouteViewModel();
                                                flightBookingMealRouteViewModel.setRoute(route);
                                                flightBookingMealRouteViewModel.setMealViewModels(flightBookingMealViewModels);
                                                flightBookingMealRouteViewModels.add(flightBookingMealRouteViewModel);
                                            }
                                        }
                                        if (flightBookingMealRouteViewModels.size() > 0) {
                                            getView().renderPassengerMeals(flightBookingMealRouteViewModels,
                                                    getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels());
                                        }
                                    }

                                    if (getView().getLuggageViewModels() != null && getView().getLuggageViewModels().size() > 0) {
                                        List<FlightBookingLuggageRouteViewModel> flightBookingLuggageRouteViewModels = new ArrayList<>();
                                        for (Route route : departureViewModel.getRouteList()) {
                                            List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels = new ArrayList<>();
                                            for (FlightBookingLuggageViewModel flightBookingLuggageViewModel : getView().getLuggageViewModels()) {
                                                if (flightBookingLuggageViewModel.getId().contains(route.getArrivalAirport()) &&
                                                        flightBookingLuggageViewModel.getId().contains(route.getDepartureAirport())) {
                                                    flightBookingLuggageViewModels.add(flightBookingLuggageViewModel);
                                                }
                                            }
                                            if (flightBookingLuggageViewModels.size() > 0) {
                                                FlightBookingLuggageRouteViewModel flightBookingLuggageRouteViewModel = new FlightBookingLuggageRouteViewModel();
                                                flightBookingLuggageRouteViewModel.setRoute(route);
                                                flightBookingLuggageRouteViewModel.setLuggage(flightBookingLuggageViewModels);
                                                flightBookingLuggageRouteViewModels.add(flightBookingLuggageRouteViewModel);
                                            }
                                        }
                                        if (flightBookingLuggageRouteViewModels.size() > 0) {
                                            getView().renderPassengerLuggages(flightBookingLuggageRouteViewModels,
                                                    getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels());
                                        }
                                    }
                                }
                            }
                        });
            }*/
        }

        if (getView().getCurrentPassengerViewModel().getPassengerName() != null) {
            getView().renderPassengerName(getView().getCurrentPassengerViewModel().getPassengerName());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerTitle() != null) {
            getView().renderPassengerTitle(getView().getCurrentPassengerViewModel().getPassengerTitle());
        }

        if (getView().getCurrentPassengerViewModel().getPassengerBirthdate() != null) {
            getView().renderBirthdate(FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_FORMAT, FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getCurrentPassengerViewModel().getPassengerBirthdate()));
        }
    }

    @Override
    public void onSaveButtonClicked() {
        if (validateFields()) {
            getView().getCurrentPassengerViewModel().setPassengerTitle(getView().getPassengerTitle());
            getView().getCurrentPassengerViewModel().setPassengerName(getView().getPassengerName());
            getView().getCurrentPassengerViewModel().setPassengerBirthdate(
                    FlightDateUtil.formatDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, FlightDateUtil.DEFAULT_FORMAT, getView().getPassengerBirthDate())
            );
            // TODO : set passenger luggage and meals
            getView().navigateResultUpdatePassengerData(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onBirthdateClicked() {

        Date maxDate, minDate, selectedDate;

        if (isChildPassenger()) {
            Calendar twelveYearsAgo = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            twelveYearsAgo
                    .add(Calendar.DATE, -4380);
            minDate = twelveYearsAgo.getTime();
            Calendar twoYearsAgo = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            twoYearsAgo
                    .add(Calendar.DATE, -730);
            maxDate = twoYearsAgo.getTime();
            selectedDate = maxDate;
        } else {
            Calendar twoYearsAgo = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            twoYearsAgo
                    .add(Calendar.DATE, -730);
            minDate = twoYearsAgo.getTime();
            Calendar today = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
            today
                    .add(Calendar.DATE, -1);
            maxDate = today.getTime();
            selectedDate = maxDate;
        }
        if (getView().getPassengerBirthDate().length() > 0) {
            selectedDate = FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate());
        }
        getView().showBirthdatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Override
    public void onBirthdateChange(int year, int month, int date) {
        Calendar now = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, month);
        now.set(Calendar.DATE, date);
        Date newReturnDate = now.getTime();
        String birthdateStr = FlightDateUtil.dateToString(newReturnDate, FlightDateUtil.DEFAULT_VIEW_FORMAT);
        getView().renderBirthdate(birthdateStr);
    }

    @Override
    public void onPassengerLuggageClick(FlightBookingLuggageMetaViewModel flightBookingLuggageMetaViewModel) {
        FlightBookingLuggageMetaViewModel existingSelected = null;
        for (FlightBookingLuggageMetaViewModel selected : getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels()) {
            if (selected.getKey().equalsIgnoreCase(flightBookingLuggageMetaViewModel.getKey())) {
                existingSelected = selected;
            }
        }
        if (existingSelected == null) {
            existingSelected = new FlightBookingLuggageMetaViewModel();
            existingSelected.setKey(flightBookingLuggageMetaViewModel.getKey());
            existingSelected.setDescription(flightBookingLuggageMetaViewModel.getDescription());
            existingSelected.setLuggages(new ArrayList<FlightBookingLuggageViewModel>());
        }
        getView().navigateToLuggagePicker(flightBookingLuggageMetaViewModel.getLuggages(), existingSelected);
    }

    @Override
    public void onLuggageDataChange(FlightBookingLuggageMetaViewModel flightBookingLuggageMetaViewModel) {
        List<FlightBookingLuggageMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingLuggageMetaViewModels();
        int index = viewModels.indexOf(flightBookingLuggageMetaViewModel);
        if (index != -1) {
            viewModels.set(index, flightBookingLuggageMetaViewModel);
        } else {
            viewModels.add(flightBookingLuggageMetaViewModel);
        }

        getView().renderPassengerLuggages(getView().getLuggageViewModels(), viewModels);
    }

    @Override
    public void onMealDataChange(FlightBookingMealMetaViewModel flightBookingMealMetaViewModel) {
        List<FlightBookingMealMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(flightBookingMealMetaViewModel);
        if (index != -1) {
            viewModels.set(index, flightBookingMealMetaViewModel);
        } else {
            viewModels.add(flightBookingMealMetaViewModel);
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onDeleteMeal(FlightBookingMealMetaViewModel viewModel) {
        List<FlightBookingMealMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(viewModel);
        if (index != -1) {
            viewModels.set(index, viewModel);
        } else {
            viewModels.add(viewModel);
        }

        getView().renderPassengerMeals(getView().getMealViewModels(), viewModels);
    }

    @Override
    public void onOptionMeal(FlightBookingMealMetaViewModel viewModel) {
        List<FlightBookingMealMetaViewModel> viewModels = getView().getCurrentPassengerViewModel().getFlightBookingMealMetaViewModels();
        int index = viewModels.indexOf(viewModel);
        if (index != -1) {
            getView().navigateToMealPicker(viewModel.getMealViewModels(), viewModels.get(index));
        } else {
            FlightBookingMealMetaViewModel selected = new FlightBookingMealMetaViewModel();
            selected.setKey(viewModel.getKey());
            selected.setDescription(viewModel.getDescription());
            selected.setMealViewModels(new ArrayList<FlightBookingMealViewModel>());
            getView().navigateToMealPicker(viewModel.getMealViewModels(), selected);
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        Calendar twoYearsAgo = new GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"));
        twoYearsAgo
                .add(Calendar.DATE, -730);
        if (getView().getPassengerName().isEmpty() || getView().getPassengerName().length() == 0) {
            isValid = false;
            getView().showPassengerNameEmptyError(R.string.flight_booking_passenger_name_empty_error);
        } else if (getView().getPassengerTitle().isEmpty() || getView().getPassengerTitle().length() == 0) {
            isValid = false;
            getView().showPassengerTitleEmptyError(R.string.flight_bookingpassenger_title_error);
        } else if ((isChildPassenger() || isInfantPassenger()) && getView().getPassengerBirthDate().length() == 0) {
            isValid = false;
            getView().showPassengerBirthdateEmptyError(R.string.flight_booking_passenger_birthdate_empty_error);
        } else if (isChildPassenger() &&
                FlightDateUtil.removeTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate()))
                        .compareTo(FlightDateUtil.removeTime(twoYearsAgo.getTime())) > 0) {
            isValid = false;
            getView().showPassengerChildBirthdateShouldMoreThan2Years(R.string.flight_booking_passenger_birthdate_child_shoud_more_than_two_years);
        } else if (isInfantPassenger() && FlightDateUtil.removeTime(FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_VIEW_FORMAT, getView().getPassengerBirthDate()))
                .compareTo(FlightDateUtil.removeTime(twoYearsAgo.getTime())) < 0) {
            isValid = false;
            getView().showPassengerInfantBirthdateShouldNoMoreThan2Years(R.string.flight_booking_passenger_birthdate_infant_should_no_more_than_two_years);
        }
        return isValid;
    }

    private boolean isRoundTrip() {
        return getView().getReturnTripId() != null;
    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == FlightBookingPassenger.INFANT;
    }
}
