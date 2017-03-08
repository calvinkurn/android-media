package com.tokopedia.session.changephonenumber.presenter;

import com.tokopedia.session.changephonenumber.listener.ChangePhoneNumberRequestView;

/**
 * Created by nisie on 3/2/17.
 */

public class ChangePhoneNumberRequestPresenterImpl implements ChangePhoneNumberRequestPresenter {

    private final ChangePhoneNumberRequestView viewListener;

    public ChangePhoneNumberRequestPresenterImpl(ChangePhoneNumberRequestView viewListener) {
     this.viewListener = viewListener;
    }

    @Override
    public void submitRequest() {
        viewListener.onGoToThanksPage();
    }
}
