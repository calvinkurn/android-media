package com.tokopedia.flight.cancellation.view.contract;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

/**
 * @author by furqan on 11/04/18.
 */

public interface FlightCancellationReviewContract {
    interface View extends CustomerView {

        void showSuccessDialog(@StringRes int resId);

        String getInvoiceId();

        FlightCancellationWrapperViewModel getCancellationWrapperViewModel();

    }

    interface Presenter {

        void requestCancellation();

    }
}
