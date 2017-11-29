package com.tokopedia.session.login.loginphonenumber.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.login.loginphonenumber.viewlistener.LoginPhoneNumber;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberPresenter extends BaseDaggerPresenter<LoginPhoneNumber.View>
        implements LoginPhoneNumber.Presenter{

    @Inject
    public LoginPhoneNumberPresenter() {

    }

    @Override
    public void attachView(LoginPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


}
