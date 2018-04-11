package com.tokopedia.session.register.registerphonenumber.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.CheckMsisdnPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.LoginRegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.RegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.view.listener.RegisterPhoneNumber;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.CheckMsisdnRegisterPhoneNumberSubscriber;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.RegisterPhoneNumberSubscriber;

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
    private final LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase;

    @Inject
    public RegisterPhoneNumberPresenter(CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase,
                                        LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase) {
        this.checkMsisdnPhoneNumberUseCase = checkMsisdnPhoneNumberUseCase;
        this.loginRegisterPhoneNumberUseCase = loginRegisterPhoneNumberUseCase;
    }

    @Override
    public void attachView(RegisterPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnPhoneNumberUseCase.unsubscribe();
        loginRegisterPhoneNumberUseCase.unsubscribe();
    }


    @Override
    public void checkPhoneNumber(String phoneNumber) {
        getView().showLoading();
        if (isValid(phoneNumber))
            checkMsisdnPhoneNumberUseCase.execute(CheckMsisdnPhoneNumberUseCase.getParams(phoneNumber),
                    new CheckMsisdnRegisterPhoneNumberSubscriber(getView(), phoneNumber));
    }

    @Override
    public void registerPhoneNumber(String phoneNumber) {
        getView().showLoading();
        loginRegisterPhoneNumberUseCase.execute(RegisterPhoneNumberUseCase.getParams(phoneNumber),
                new RegisterPhoneNumberSubscriber(getView()));

    }
    private boolean isValid(String phoneNumber) {
        boolean isValid = true;
        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        }
        return isValid;
    }
}
