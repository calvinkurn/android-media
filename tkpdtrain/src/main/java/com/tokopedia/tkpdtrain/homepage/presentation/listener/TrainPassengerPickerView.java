package com.tokopedia.tkpdtrain.homepage.presentation.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;

/**
 * @author Rizky on 13/03/18.
 */

public interface TrainPassengerPickerView extends CustomerView {

    void renderPassengerView(TrainPassengerViewModel trainPassengerViewModel);

    void showTotalPassengerErrorMessage(int select_passenger_total_passenger_error_message);

    void showAdultCantBeGreaterThanFourErrorMessage(int select_passenger_adult_cant_be_greater_than_four_error_message);

    void showInfantGreaterThanAdultErrorMessage(int select_passenger_infant_greater_than_adult_error_message);

    void showAdultShouldAtleastOneErrorMessage(int select_passenger_adult_atleast_one_error_message);

    void actionNavigateBack(TrainPassengerViewModel currentPassengerPassData);

    TrainPassengerViewModel getCurrentPassengerViewModel();

}
