package com.tokopedia.otp.phoneverification.view.listener;

import android.app.Activity;
import android.widget.EditText;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by nisie on 2/24/17.
 */
public interface ChangePhoneNumber {

    interface View extends CustomerView {

        void onSuccessChangePhoneNumber();

        EditText getPhoneNumberEditText();

        String getString(int resId);

        Activity getActivity();

        void onErrorChangePhoneNumber(String errorMessage);
    }

    interface Presenter extends CustomerPresenter<View> {
        void changePhoneNumber(String phoneNumber);
    }

}