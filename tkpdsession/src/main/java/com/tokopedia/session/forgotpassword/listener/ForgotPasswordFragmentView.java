package com.tokopedia.session.forgotpassword.listener;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

/**
 * Created by Alifa on 10/17/2016.
 */

public interface ForgotPasswordFragmentView {
    Context getContext();

    Activity getActivity();

    String getString(int resId);

    void refresh();

    void resetError();

    EditText getEmail();

    void setEmailError(String errorMessage);

    void showLoadingProgress();

    void onErrorResetPassword(String errorMessage);

    void onSuccessResetPassword();
}
