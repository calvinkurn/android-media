package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.domain.interactor.registerphonenumber.CheckMsisdnPhoneNumberUseCase;
import com.tokopedia.session.register.view.subscriber.registerphonenumber.CheckMsisdnRegisterPhoneNumberSubscriber;
import com.tokopedia.session.register.view.viewlistener.RegisterPhoneNumber;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberPresenter extends BaseDaggerPresenter<RegisterPhoneNumber.View>
        implements RegisterPhoneNumber.Presenter {
    private final CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase;

    @Inject
    public RegisterPhoneNumberPresenter(CheckMsisdnPhoneNumberUseCase checkMsisdnPhoneNumberUseCase) {
        this.checkMsisdnPhoneNumberUseCase = checkMsisdnPhoneNumberUseCase;
    }

    @Override
    public void attachView(RegisterPhoneNumber.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        checkMsisdnPhoneNumberUseCase.unsubscribe();
    }


    @Override
    public void registerWithPhoneNumber(String phoneNumber) {
        if (isValid(phoneNumber)) {
            getView().showLoading();
            checkMsisdnPhoneNumberUseCase.execute(CheckMsisdnPhoneNumberUseCase.getParams(phoneNumber),
                    new CheckMsisdnRegisterPhoneNumberSubscriber(getView(), phoneNumber));
        }
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
}
