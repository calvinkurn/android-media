package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.homepage.presentation.TrainPassengerPickerValidator;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.TrainPassengerPickerView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;

/**
 * @author Rizky on 13/03/18.
 */

public class TrainPassengerPickerPresenterImpl extends BaseDaggerPresenter<TrainPassengerPickerView> implements TrainPassengerPickerPresenter {

    public TrainPassengerPickerPresenterImpl() {

    }

    @Override
    public void initialize() {
        getView().renderPassengerView(getView().getCurrentPassengerViewModel());
    }

    @Override
    public void onAdultPassengerCountChange(int number) {
        TrainPassengerViewModel passengerViewModel = clonePassData(getView().getCurrentPassengerViewModel());
        passengerViewModel.setAdult(number);
        if (validatePassenger(passengerViewModel)) {
            getView().renderPassengerView(passengerViewModel);
        } else {
            getView().renderPassengerView(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onInfantPassengerCountChange(int number) {
        TrainPassengerViewModel passengerPassData = clonePassData(getView().getCurrentPassengerViewModel());
        passengerPassData.setInfant(number);
        if (validatePassenger(passengerPassData)) {
            getView().renderPassengerView(passengerPassData);
        } else {
            getView().renderPassengerView(getView().getCurrentPassengerViewModel());
        }
    }

    @Override
    public void onSaveButtonClicked() {
        TrainPassengerViewModel passengerPassData = clonePassData(getView().getCurrentPassengerViewModel());
        if (validatePassenger(passengerPassData)) {
            getView().actionNavigateBack(passengerPassData);
        }
    }

    private boolean validatePassenger(TrainPassengerViewModel passengerPassData) {
        TrainPassengerPickerValidator validator = new TrainPassengerPickerValidator();
        boolean isValid = true;
        if (!validator.validateTotalPassenger(passengerPassData)) {
            isValid = false;
            getView().showTotalPassengerErrorMessage(R.string.select_passenger_total_passenger_error_message);
        } else if (!validator.validateAdultNotGreaterThanFour(passengerPassData)) {
            isValid = false;
            getView().showAdultCantBeGreaterThanFourErrorMessage(R.string.select_passenger_adult_cant_be_greater_than_four_error_message);
        } else if (!validator.validateInfantNotGreaterThanAdult(passengerPassData)) {
            isValid = false;
            getView().showInfantGreaterThanAdultErrorMessage(R.string.select_passenger_infant_greater_than_adult_error_message);
        } else if (!validator.validateAdultCountAtleastOne(passengerPassData)) {
            isValid = false;
            getView().showAdultShouldAtleastOneErrorMessage(R.string.select_passenger_adult_atleast_one_error_message);
        }
        return isValid;
    }

    private TrainPassengerViewModel clonePassData(TrainPassengerViewModel passengerViewModel) {
        TrainPassengerViewModel trainPassengerViewModel = null;
        try {
            trainPassengerViewModel = (TrainPassengerViewModel) passengerViewModel.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("CloneNotSupportedException TrainPassengerViewModel");
        }
        return trainPassengerViewModel;
    }

}
