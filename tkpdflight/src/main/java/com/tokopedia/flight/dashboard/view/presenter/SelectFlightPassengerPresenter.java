package com.tokopedia.flight.dashboard.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 10/26/17.
 */

public interface SelectFlightPassengerPresenter extends CustomerPresenter<SelectFlightPassengerView>{
    void onAdultPassengerCountChange(int number);

    void onChildrenPassengerCountChange(int number);

    void onInfantPassengerCountChange(int number);

    void initialize();

    void onSaveButtonClicked();

}
