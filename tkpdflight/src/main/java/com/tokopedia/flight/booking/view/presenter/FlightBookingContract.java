package com.tokopedia.flight.booking.view.presenter;

import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 11/8/17.
 */

public interface FlightBookingContract {
    interface View extends CustomerView {

        String getContactName();

        void showContactNameEmptyError(@StringRes int resId);

        String getContactEmail();

        void showContactEmailEmptyError(@StringRes int resId);

        void showContactEmailInvalidError(@StringRes int resId);

        String getContactPhoneNumber();

        void showContactPhoneNumberEmptyError(@StringRes int resId);
    }

    interface Presenter extends CustomerPresenter<View> {

        void onButtonSubmitClicked();
    }
}
