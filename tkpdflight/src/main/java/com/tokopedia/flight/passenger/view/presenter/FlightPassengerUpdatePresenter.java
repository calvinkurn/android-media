package com.tokopedia.flight.passenger.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.passenger.domain.FlightBookingGetSavedPassengerUseCase;
import com.tokopedia.flight.passenger.domain.FlightPassengerGetSingleUseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.ADULT;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.CHILDREN;
import static com.tokopedia.flight.booking.constant.FlightBookingPassenger.INFANT;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdatePresenter extends BaseDaggerPresenter<FlightPassengerUpdateContract.View>
        implements FlightPassengerUpdateContract.Presenter {

    FlightPassengerGetSingleUseCase flightBookingGetSinglePassengerUseCase;

    @Inject
    public FlightPassengerUpdatePresenter(FlightPassengerGetSingleUseCase flightBookingGetSinglePassengerUseCase) {
        this.flightBookingGetSinglePassengerUseCase = flightBookingGetSinglePassengerUseCase;
    }

    @Override
    public void onViewCreated() {
        getPassengerDataFromDb();
    }

    private void getPassengerDataFromDb() {
        flightBookingGetSinglePassengerUseCase.execute(
                flightBookingGetSinglePassengerUseCase.generateRequestParams(
                        getView().getPassengerId()
                ),
                new Subscriber<FlightBookingPassengerViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
                        getView().setPassengerViewModel(flightBookingPassengerViewModel);
                        renderView();
                    }
                }
        );
    }

    private void renderView() {

        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getView().getCurrentPassengerViewModel();
        flightBookingPassengerViewModel.setPassengerTitle(
                getSalutationById(flightBookingPassengerViewModel.getPassengerTitleId()));

        if (isAdultPassenger()) {
            getView().renderSpinnerForAdult();
            getView().renderPassengerType(
                    getView().getString(R.string.flightbooking_price_adult_label));
        } else {
            getView().renderSpinnerForChildAndInfant();

            if (isChildPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_child_label));
            } else if (isInfantPassenger()) {
                getView().renderPassengerType(
                        getView().getString(R.string.flightbooking_price_infant_label));
            }
        }

        if (flightBookingPassengerViewModel.getPassengerBirthdate() != null) {
            getView().renderPassengerBirthdate(
                    FlightDateUtil.dateToString(
                            FlightDateUtil.stringToDate(FlightDateUtil.DEFAULT_FORMAT,
                                    flightBookingPassengerViewModel.getPassengerBirthdate()),
                            FlightDateUtil.DEFAULT_VIEW_FORMAT
                    )
            );
        }

        getView().renderSelectedTitle(flightBookingPassengerViewModel.getPassengerTitle());
        getView().renderPassengerName(flightBookingPassengerViewModel.getPassengerFirstName(),
                flightBookingPassengerViewModel.getPassengerLastName());

    }

    private boolean isAdultPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == ADULT;
    }

    private boolean isChildPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == CHILDREN;
    }

    private boolean isInfantPassenger() {
        return getView().getCurrentPassengerViewModel().getType() == INFANT;
    }

    private String getSalutationById(int salutationId) {
        switch (salutationId) {
            case TUAN:
                return getView().getString(R.string.mister);
            case NYONYA:
                return getView().getString(R.string.misiz);
            case NONA:
                return getView().getString(R.string.miss);
        }

        return "";
    }

}
