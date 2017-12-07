package com.tokopedia.session.login.loginphonenumber.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.domain.interactor.CheckMsisdnTokoCashUseCase;
import com.tokopedia.session.login.loginphonenumber.view.subscriber.CheckMsisdnTokoCashSubscriber;
import com.tokopedia.session.login.loginphonenumber.view.viewlistener.LoginPhoneNumber;

import javax.inject.Inject;

/**
 * @author by nisie on 11/23/17.
 */

public class LoginPhoneNumberPresenter extends BaseDaggerPresenter<LoginPhoneNumber.View>
        implements LoginPhoneNumber.Presenter {

    private final CheckMsisdnTokoCashUseCase checkMsisdnTokoCashUseCase;

    @Inject
    public LoginPhoneNumberPresenter(CheckMsisdnTokoCashUseCase checkMsisdnTokoCashUseCase) {
        this.checkMsisdnTokoCashUseCase = checkMsisdnTokoCashUseCase;
    }

    @Override
    public void attachView(LoginPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnTokoCashUseCase.unsubscribe();
    }


    @Override
    public void loginWithPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            getView().showLoading();
            checkMsisdnTokoCashUseCase.execute(CheckMsisdnTokoCashUseCase.getParam(phoneNumber),
                    new CheckMsisdnTokoCashSubscriber(getView(), phoneNumber));
        }
    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        }else if(phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            getView().showErrorPhoneNumber(R.string.phone_number_invalid);
            isValid = false;
        }
        return isValid;
    }
}
