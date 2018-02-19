package com.tokopedia.session.changephonenumber.view.presenter;

import android.text.Editable;

/**
 * Created by nisie on 3/2/17.
 */

public interface ChangePhoneNumberRequestPresenter {
    void continueToNext();

    void submitRequest(String phoneNumber);

    void checkStatus();

    void setIdImage(String idPath);

    void setBankBookImage(String bankBookPath);

    boolean isValidParam();

    void onDestroyView();

    void onNewNumberTextChanged(Editable editable, int selectionStart);
}
