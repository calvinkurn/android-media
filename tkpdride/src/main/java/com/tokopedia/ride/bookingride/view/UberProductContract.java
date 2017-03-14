package com.tokopedia.ride.bookingride.view;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface UberProductContract {

    interface View extends CustomerView {
        void showMessage(String message);

        void hideMessage(String message);
    }

    interface Presenter extends CustomerPresenter<View> {
        void initialize();
    }
}
