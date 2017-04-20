package com.tokopedia.ride.history.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 4/20/17.
 */

public interface RideHistoryDetailContract {

    interface View extends CustomerView {

    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
