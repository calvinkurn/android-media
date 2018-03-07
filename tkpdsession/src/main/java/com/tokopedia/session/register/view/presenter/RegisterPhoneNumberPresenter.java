package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.data.pojo.RegisterPhoneNumberData;
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

    @Inject
    public RegisterPhoneNumberPresenter(CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase,
                                        RegisterPhoneNumberUseCase registerPhoneNumberUseCase) {
        this.checkMsisdnPhoneNumberUseCase = checkMsisdnPhoneNumberUseCase;
        this.registerPhoneNumberUseCase = registerPhoneNumberUseCase;
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
    public void startAction(RegisterPhoneNumberData data) {
        switch (data.getAction()) {
//            case GO_TO_LOGIN:
//                viewListener.goToAutomaticLogin();
//                break;
//            case GO_TO_REGISTER:
//            case GO_TO_ACTIVATION_PAGE:
//                if (data.getIsActive() == STATUS_ACTIVE)
//                    viewListener.goToAutomaticLogin();
//                else if (data.getIsActive() == STATUS_INACTIVE || data.getIsActive() == STATUS_PENDING)
//                    viewListener.goToActivationPage(data);
//                break;
//            case GO_TO_RESET_PASSWORD:
//                viewListener.showInfo();
//                break;
//            default:
//                viewListener.onErrorRegister(ErrorHandler.getDefaultErrorCodeMessage(ErrorCode.UNSUPPORTED_FLOW));
        }
    }
}
