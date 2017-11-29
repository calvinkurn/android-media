package com.tokopedia.session.login.loginphonenumber.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;
import com.tokopedia.session.login.loginphonenumber.viewlistener.LoginPhoneNumber;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberPresenter extends BaseDaggerPresenter<LoginPhoneNumber.View>
        implements LoginPhoneNumber.Presenter {

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


    @Override
    public void loginWithPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            getView().goToVerifyAccountPage(phoneNumber);
        }
    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;

        if(TextUtils.isEmpty(phoneNumber)){
            getView().showErrorPhoneNumber(R.string.error_field_required);
        }
        return isValid;
    }
}
