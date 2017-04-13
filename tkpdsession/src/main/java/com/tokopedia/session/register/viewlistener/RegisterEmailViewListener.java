package com.tokopedia.session.register.viewlistener;

import android.app.Activity;
import android.widget.EditText;

import com.tokopedia.session.register.model.RegisterViewModel;
import com.tokopedia.session.register.model.gson.RegisterResult;
import com.tokopedia.session.register.viewmodel.RegisterEmailViewModel;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterEmailViewListener {

    EditText getName();

    EditText getEmail();

    EditText getPassword();

    void resetError();

    void setPasswordError(String messageError);

    Activity getActivity();

    EditText getPhone();

    void setPhoneError(String errorMessage);

    void setEmailError(String messageError);

    void setNameError(String messageError);

    String getString(int resId);

    void showSnackbar(String message);

    void setActionsEnabled(boolean isEnabled);

    void showLoadingProgress();

    void dismissLoadingProgress();

    void goToRegisterStep2();

    void goToActivationPage();

    void goToAutomaticResetPassword();

    void goToAutomaticLogin();

    void dropKeyboard();

    void onErrorRegister(String errorMessage);

    void onSuccessRegister(RegisterEmailViewModel registerResult);

    void setRegisterModel(RegisterViewModel registerViewModel);
}
