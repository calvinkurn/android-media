package com.tokopedia.session.changephonenumber.view.listener;

import android.app.Activity;

import com.tokopedia.session.changephonenumber.data.model.changephonenumberrequest.CheckStatusData;


/**
 * Created by nisie on 3/2/17.
 */
public interface ChangePhoneNumberRequestView {
    void onGoToWaitPage();

    void showLoading();

    Activity getActivity();

    void onSuccessCheckStatus(CheckStatusData checkStatusModel);

    void onErrorcheckStatus(String errorMessage);

    String getString(int resId);

    void onErrorSubmitRequest(String errorMessage);

    void onSuccessSubmitRequest();
}
