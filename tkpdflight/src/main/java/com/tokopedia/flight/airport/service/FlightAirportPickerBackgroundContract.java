package com.tokopedia.flight.airport.service;

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;

/**
 * Created by zulfikarrahman on 11/21/17.
 */

public interface FlightAirportPickerBackgroundContract {
    interface View extends CustomerView{

        void onGetAirportError(Throwable e);

        void onGetAirport(Boolean isSuccess);
    }

    interface Presenter extends CustomerPresenter<View>{
        void getAirportListCloud();
    }
}
