package com.tokopedia.otp.phoneverification.view.listener;

import android.app.Activity;
import android.widget.EditText;

/**
 * Created by nisie on 2/24/17.
 */
public interface ChangePhoneNumberView {
    void onSuccessChangePhoneNumber();

    EditText getPhoneNumberEditText();

    String getString(int resId);

    Activity getActivity();

    void onErrorChangePhoneNumber(String errorMessage);
}
