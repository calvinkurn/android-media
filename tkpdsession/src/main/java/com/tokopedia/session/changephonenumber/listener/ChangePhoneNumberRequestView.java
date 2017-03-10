package com.tokopedia.session.changephonenumber.listener;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.core.network.entity.changephonenumberrequest.CheckStatusData;
import com.tokopedia.session.changephonenumber.data.CheckStatusModel;

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
