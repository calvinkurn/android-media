package com.tokopedia.session.register.view.viewlistener;

import android.app.Activity;
import android.widget.EditText;

import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;

/**
 * Created by nisie on 1/27/17.
 */
public interface RegisterEmailViewListener {

    EditText getName();

    EditText getEmail();

    EditText getPassword();

    void resetError();

    Activity getActivity();

    EditText getPhone();

    String getString(int resId);

    void setActionsEnabled(boolean isEnabled);

    void showLoadingProgress();

    void dismissLoadingProgress();

    void goToActivationPage(RegisterEmailViewModel viewModel);

    void goToAutomaticResetPassword();

    void goToAutomaticLogin();

    void dropKeyboard();

    void onErrorRegister(String errorMessage);

    void onSuccessRegister(RegisterEmailViewModel registerResult);

    void getRegisterModel(RegisterViewModel registerViewModel);

    void showInfo();
}
