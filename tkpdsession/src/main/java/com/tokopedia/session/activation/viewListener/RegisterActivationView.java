package com.tokopedia.session.activation.viewListener;

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
}
