package com.tokopedia.flight.booking.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.domain.FlightBookingGetSavedPassengerUseCase;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.util.FlightDateUtil;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NONA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.NYONYA;
import static com.tokopedia.flight.common.util.FlightPassengerTitleType.TUAN;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightBookingListPassengerPresenter extends BaseDaggerPresenter<FlightBookingListPassengerContract.View> implements FlightBookingListPassengerContract.Presenter {

    private FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase;

    private static int TWELVE_YEARS = 12;
    private static int TWO_YEARS = 2;

    @Inject
    public FlightBookingListPassengerPresenter(FlightBookingGetSavedPassengerUseCase flightBookingGetSavedPassengerUseCase) {
        this.flightBookingGetSavedPassengerUseCase = flightBookingGetSavedPassengerUseCase;
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

    private void getSavedPassengerList() {
        flightBookingGetSavedPassengerUseCase.execute(
                flightBookingGetSavedPassengerUseCase.createEmptyRequestParams(),
                new Subscriber<List<FlightBookingPassengerViewModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<FlightBookingPassengerViewModel> flightBookingPassengerViewModels) {
                        formatPassenger(flightBookingPassengerViewModels);
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

            localId++;
        }

        getView().setPassengerViewModelList(flightBookingPassengerViewModelList);
        getView().renderPassengerList();
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
            long year = (1000 * 60 * 60 * 24 * 365);

            if (diff > (TWELVE_YEARS * year)) {
                if (salutationId == TUAN) {
                    return R.drawable.ic_passenger_male;
                } else {
                    return R.drawable.ic_passenger_female;
                }
            } else if (diff > (TWO_YEARS * year)) {
                return R.drawable.ic_passenger_childreen;
            } else if (diff < (TWO_YEARS * year)) {
                return R.drawable.ic_passenger_infant;
            }
        }

        return R.drawable.ic_passenger_male;
    }
}
