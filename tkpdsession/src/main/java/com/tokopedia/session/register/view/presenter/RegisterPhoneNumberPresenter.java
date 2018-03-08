package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.login.loginphonenumber.domain.interactor.LoginPhoneNumberUseCase;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.domain.interactor.registerphonenumber.CheckMsisdnPhoneNumberUseCase;
import com.tokopedia.session.register.domain.interactor.registerphonenumber.RegisterPhoneNumberUseCase;
import com.tokopedia.session.register.view.subscriber.registerphonenumber.CheckMsisdnRegisterPhoneNumberSubscriber;
import com.tokopedia.session.register.view.subscriber.registerphonenumber.RegisterPhoneNumberSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberPresenter extends BaseDaggerPresenter<RegisterPhoneNumber.View>
        implements RegisterPhoneNumber.Presenter {

    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_PENDING = -1;
    private static final int STATUS_INACTIVE = 0;

    private final CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase;
    private final RegisterPhoneNumberUseCase registerPhoneNumberUseCase;
    private final LoginPhoneNumberUseCase loginPhoneNumberUseCase;

    @Inject
    public RegisterPhoneNumberPresenter(CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase,
                                        RegisterPhoneNumberUseCase registerPhoneNumberUseCase,
                                        LoginPhoneNumberUseCase loginPhoneNumberUseCase) {
        this.checkMsisdnPhoneNumberUseCase = checkMsisdnPhoneNumberUseCase;
        this.registerPhoneNumberUseCase = registerPhoneNumberUseCase;
        this.loginPhoneNumberUseCase = loginPhoneNumberUseCase;
    }

    @Override
    public void attachView(RegisterPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnPhoneNumberUseCase.unsubscribe();
        registerPhoneNumberUseCase.unsubscribe();
        loginPhoneNumberUseCase.unsubscribe();
    }


    @Override
    public void checkPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            getView().showLoading();
            checkMsisdnPhoneNumberUseCase.execute(CheckMsisdnPhoneNumberUseCase.getParams(phoneNumber),
                    new CheckMsisdnRegisterPhoneNumberSubscriber(getView(), phoneNumber));
        }
    }

    @Override
    public void registerPhoneNumber(String phoneNumber) {
        getView().showLoading();
        registerPhoneNumberUseCase.execute(RegisterPhoneNumberUseCase.getParams(phoneNumber),
                new RegisterPhoneNumberSubscriber(getView()));

    }

    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        } else if (phoneNumber.length() < 7 || phoneNumber.length() > 15) {
            getView().showErrorPhoneNumber(R.string.phone_number_invalid);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void loginWithPhoneNumber(String phoneNumber,
                                     RegisterPhoneNumberModel registerPhoneNumberModel) {
        loginPhoneNumberUseCase.execute(LoginPhoneNumberUseCase.getParam());
    }
}
