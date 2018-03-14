package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.constant.FlightBookingPassenger;
import com.tokopedia.flight.passenger.domain.FlightBookingDeletePassengerUseCase;
import com.tokopedia.flight.passenger.domain.FlightBookingGetSavedPassengerUseCase;
import com.tokopedia.flight.passenger.domain.FlightBookingUpdateSelectedPassengerUseCase;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingListPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.usecase.RequestParams;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingListPassengerPresenter extends BaseDaggerPresenter<FlightBookingListPassengerContract.View> implements FlightBookingListPassengerContract.Presenter {

    private FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase;
    private FlightBookingUpdateSelectedPassengerUseCase flightBookingUpdateSelectedPassengerUseCase;
    private FlightBookingDeletePassengerUseCase flightBookingDeletePassengerUseCase;

    private static long YEAR_IN_MILLIS = TimeUnit.DAYS.toMillis(365);
    private static long TWELVE_YEARS_IN_MILLIS = 12 * YEAR_IN_MILLIS;
    private static long TWO_YEARS_IN_MILLIS = 2 * YEAR_IN_MILLIS;

    private CompositeSubscription compositeSubscription;


    @Inject
    public FlightBookingListPassengerPresenter(FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase,
                                               FlightBookingUpdateSelectedPassengerUseCase flightBookingUpdateSelectedPassengerUseCase,
                                               FlightBookingDeletePassengerUseCase flightBookingDeletePassengerUseCase) {
        this.flightBookingGetSavedPassengerUseCase = flightBookingGetSavedPassengerUseCase;
        this.flightBookingUpdateSelectedPassengerUseCase = flightBookingUpdateSelectedPassengerUseCase;
        this.flightBookingDeletePassengerUseCase = flightBookingDeletePassengerUseCase;
        compositeSubscription = new CompositeSubscription();
    }


    @Override
    public void onViewCreated() {
        getSavedPassengerList();
    }

    @Override
    public boolean isPassengerSame(FlightBookingPassengerViewModel selectedPassenger) {
        FlightBookingPassengerViewModel currentPassenger = getView().getCurrentPassenger();

        if (currentPassenger.getPassengerId() != null &&
                selectedPassenger.getPassengerId() != null) {
            if (currentPassenger.getPassengerId()
                    .equals(selectedPassenger.getPassengerId())) {
                return true;
            }
        }

        if (currentPassenger.getPassengerBirthdate() != null &&
                selectedPassenger.getPassengerBirthdate() != null) {
            if (currentPassenger.getPassengerBirthdate()
                    .equals(selectedPassenger.getPassengerBirthdate()) &&
                    currentPassenger.getPassengerFirstName()
                            .equals(selectedPassenger.getPassengerFirstName()) &&
                    currentPassenger.getPassengerLastName()
                            .equals(selectedPassenger.getPassengerLastName())) {
                return true;
            }
        }

        if (currentPassenger.getPassengerFirstName()
                .equals(selectedPassenger.getPassengerFirstName()) &&
                currentPassenger.getPassengerLastName()
                        .equals(selectedPassenger.getPassengerLastName())) {
            return true;
        }

        return false;
    }

    @Override
    public void selectPassenger(FlightBookingPassengerViewModel selectedPassenger) {
        if (getView().getCurrentPassenger() != null &&
                selectedPassenger != null &&
                getView().getCurrentPassenger().getType() == selectedPassenger.getType()) {
            if (selectedPassenger != null) {
                onSelectPassenger(selectedPassenger);
            }
        } else if (getView().getCurrentPassenger() != null &&
                selectedPassenger != null &&
                getView().getCurrentPassenger().getType() != selectedPassenger.getType()) {
            String passengerType = "";
            switch (getView().getCurrentPassenger().getType()) {
                case FlightBookingPassenger.ADULT:
                    passengerType = getView().getString(R.string.select_passenger_adult_title);
                    break;
                case FlightBookingPassenger.CHILDREN:
                    passengerType = getView().getString(R.string.select_passenger_children_title);
                    break;
                case FlightBookingPassenger.INFANT:
                    passengerType = getView().getString(R.string.select_passenger_infant_title);
                    break;
            }
            getView().showPassengerSelectedError(passengerType);
        } else if (selectedPassenger == null) {
            onUnselectPassenger(getView().getCurrentPassenger().getPassengerId());
        }
    }

    @Override
    public void onDestroyView() {
        if (compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void deletePassenger(String passengerId) {
        flightBookingDeletePassengerUseCase.execute(
                flightBookingDeletePassengerUseCase.generateRequest(passengerId,
                        getView().getRequestId()),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean response) {
                        getSavedPassengerList();
                    }
                }
        );
    }

    private void getSavedPassengerList() {
        flightBookingGetSavedPassengerUseCase.execute(
                getSavedPassengerRequestParams(),
                new Subscriber<List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onGetListError(e);

                    }

                    @Override
                    public void onNext(List<FlightBookingPassengerViewModel> flightBookingPassengerViewModels) {
                        formatPassenger(flightBookingPassengerViewModels);
                    }
                }
        );
    }

    private RequestParams getSavedPassengerRequestParams() {
        if (getView().getCurrentPassenger().getPassengerId() != null) {
            return flightBookingGetSavedPassengerUseCase.generateRequestParams(getView().getCurrentPassenger().getPassengerId());
        } else {
            return flightBookingGetSavedPassengerUseCase.createEmptyRequestParams();
        }
    }

    private void onSelectPassenger(final FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        compositeSubscription.add(
                Observable.zip(
                        flightBookingUpdateSelectedPassengerUseCase.createObservable(
                                flightBookingUpdateSelectedPassengerUseCase.createRequestParams(
                                        flightBookingPassengerViewModel.getPassengerId(),
                                        FlightBookingListPassengerFragment.IS_SELECTING
                                )
                        ),
                        flightBookingUpdateSelectedPassengerUseCase.createObservable(
                                flightBookingUpdateSelectedPassengerUseCase.createRequestParams(
                                        getView().getSelectedPassengerId(),
                                        FlightBookingListPassengerFragment.IS_NOT_SELECTING
                                )
                        ),
                        new Func2<Boolean, Boolean, Boolean>() {
                            @Override
                            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
                                return true;
                            }
                        }
                )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {
                                throwable.printStackTrace();
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                getView().setCurrentPassanger(flightBookingPassengerViewModel);
                                getView().onSelectPassengerSuccess(flightBookingPassengerViewModel);
                            }
                        })
        );
    }

    private void onUnselectPassenger(String passengerId) {
        flightBookingUpdateSelectedPassengerUseCase.execute(
                flightBookingUpdateSelectedPassengerUseCase.createRequestParams(
                        passengerId,
                        FlightBookingListPassengerFragment.IS_NOT_SELECTING
                ),
                new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getView().onSelectPassengerSuccess(null);
                    }
                }
        );
    }

    private void formatPassenger(List<FlightBookingPassengerViewModel> data) {
        int localId = 1;
        List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList = data;

        for (FlightBookingPassengerViewModel flightBookingPassengerViewModel : flightBookingPassengerViewModelList) {
            flightBookingPassengerViewModel.setPassengerLocalId(localId);
            flightBookingPassengerViewModel.setPassengerTitle(
                    getSalutationById(flightBookingPassengerViewModel.getPassengerTitleId())
            );
            flightBookingPassengerViewModel.setPassengerDrawable(
                    getImageRes(
                            flightBookingPassengerViewModel.getPassengerBirthdate(),
                            flightBookingPassengerViewModel.getPassengerTitleId()
                    )
            );
            flightBookingPassengerViewModel.setType(
                    getType(
                            flightBookingPassengerViewModel.getPassengerBirthdate()
                    )
            );

            localId++;
        }

        getView().setPassengerViewModelList(flightBookingPassengerViewModelList);
        getView().renderPassengerList();
    }

    private int getType(String birthdate) {
        if (birthdate != null) {
            Date now = FlightDateUtil.getCurrentDate();
            Date birth = FlightDateUtil.stringToDate(birthdate);
            long diff = birth.getTime() - now.getTime();
            if (diff < 0) {
                diff *= -1;
            }

            if (diff > TWELVE_YEARS_IN_MILLIS) {
                return ADULT;
            } else if (diff > TWO_YEARS_IN_MILLIS) {
                return CHILDREN;
            } else if (diff < TWO_YEARS_IN_MILLIS) {
                return INFANT;
            }
        }

        return ADULT;
    }

    private String getSalutationById(int salutationId) {
        switch (salutationId) {
            case TUAN:
                return getView().getSalutationString(R.string.mister);
            case NYONYA:
                return getView().getSalutationString(R.string.misiz);
            case NONA:
                return getView().getSalutationString(R.string.miss);
        }

        return "";
    }

    private int getImageRes(String birthdate, int salutationId) {
        if (birthdate != null) {
            Date now = FlightDateUtil.getCurrentDate();
            Date birth = FlightDateUtil.stringToDate(birthdate);
            long diff = now.getTime() - birth.getTime();

            if (diff > (TWELVE_YEARS_IN_MILLIS)) {
                if (salutationId == TUAN) {
                    return R.drawable.ic_passenger_male;
                } else {
                    return R.drawable.ic_passenger_female;
                }
            } else if (diff > (TWO_YEARS_IN_MILLIS)) {
                return R.drawable.ic_passenger_childreen;
            } else if (diff < (TWO_YEARS_IN_MILLIS)) {
                return R.drawable.ic_passenger_infant;
            }
        }

        return R.drawable.ic_passenger_male;
    }

}

