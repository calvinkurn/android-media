package com.tokopedia.ride.bookingride.view;

import android.content.Context;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 4/25/17.
 */

public interface ManagePaymentOptionsContract {
    interface View extends CustomerView {
        Context getActivity();
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
