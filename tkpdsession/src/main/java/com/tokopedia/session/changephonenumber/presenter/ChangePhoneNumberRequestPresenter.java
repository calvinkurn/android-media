package com.tokopedia.session.changephonenumber.presenter;

import android.view.View;

import com.tokopedia.session.changephonenumber.data.ChangePhoneNumberRequestPass;

/**
 * Created by nisie on 3/2/17.
 */

public interface ChangePhoneNumberRequestPresenter {
    void submitRequest();

    void checkStatus();

    void setIdImage(String idPath);

    void setBankBookImage(String bankBookPath);

    boolean isValidParam();

    void onDestroyView();

}
