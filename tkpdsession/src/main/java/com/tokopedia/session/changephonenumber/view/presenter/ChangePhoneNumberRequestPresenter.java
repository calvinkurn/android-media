package com.tokopedia.session.changephonenumber.view.presenter;

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
