package com.tokopedia.transaction.base;

import android.app.Dialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;

/**
 * @author anggaprasetiyo on 1/9/17.
 */

public interface IBaseView {

    void navigateToActivityRequest(Intent intent, int requestCode);

    void navigateToActivity(Intent intent);

    void showProgressLoading();

    void hideProgressLoading();

    void showToastMessage(String message);

    void showDialog(Dialog dialog);

    void dismissDialog(Dialog dialog);

    void executeIntentService(Bundle bundle, Class<? extends IntentService> clazz);

    String getStringFromResource(@StringRes int resId);

    void closeView();
}
