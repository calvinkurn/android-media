package com.tokopedia.session.activation.view.viewListener;

import android.app.Activity;

import com.tokopedia.session.activation.view.viewmodel.LoginTokenViewModel;

/**
 * Created by nisie on 1/31/17.
 */
public interface RegisterActivationView {
    void showLoadingProgress();

    void finishLoadingProgress();

    String getString(int resId);

    void onErrorResendActivation(String errorMessage);

    void onSuccessResendActivation(String statusMessage);

    String getEmail();

    Activity getActivity();

    String getUnicode();

    void onErrorActivateWithUnicode(String errorMessage);

    void onSuccessActivateWithUnicode(LoginTokenViewModel statusMessage);
}
