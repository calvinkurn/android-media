package com.tokopedia.session.changephonenumber.presenter;

import android.view.View;

/**
 * Created by nisie on 3/2/17.
 */

public interface ChangePhoneNumberRequestPresenter {
    void submitRequest();

    void checkStatus();

    void setIdPath(String idPath);

    void setBankBookPath(String bankBookPath);
}
