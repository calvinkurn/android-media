package com.tokopedia.session.register.viewlistener;

import android.app.Activity;
import android.widget.EditText;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterStep1ViewListener {

    EditText getName();

    EditText getEmail();

    EditText getPassword();

    void resetError();

    void setPasswordError(String messageError);

    Activity getActivity();

    void setEmailError(String messageError);

    void setNameError(String messageError);

    String getString(int resId);

    void showSnackbar(String message);

    void setActionsEnabled(boolean isEnabled);

    void onErrorValidateEmail(String errorMessage);

    void showLoadingProgress();

    void dismissLoadingProgress();

    void goToRegisterStep2();

    void goToActivationPage();

    void goToAutomaticResetPassword();

    void goToAutomaticLogin();

}
