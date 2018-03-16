package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

/**
 * Created by Rizky on 13/03/18.
 */

public interface TrainPassengerPickerPresenter {

    void initialize();

    void onAdultPassengerCountChange(int number);

    void onInfantPassengerCountChange(int number);

    void onSaveButtonClicked();

}
