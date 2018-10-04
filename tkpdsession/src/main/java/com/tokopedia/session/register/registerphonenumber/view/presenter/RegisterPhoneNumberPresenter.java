package com.tokopedia.session.register.registerphonenumber.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.CheckMsisdnPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.view.listener.RegisterPhoneNumber;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.CheckMsisdnRegisterPhoneNumberSubscriber;

import javax.inject.Inject;

/**
 * @author by yfsx on 26/2/18.
 */

public class RegisterPhoneNumberPresenter extends BaseDaggerPresenter<RegisterPhoneNumber.View>
        implements RegisterPhoneNumber.Presenter {

    public static final int COUNT_MIN = 8;
    public static final int COUNT_MAX = 15;

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
    public void checkPhoneNumber(String phoneNumber) {
        getView().showLoading();
        if (isValid(phoneNumber))
            checkMsisdnPhoneNumberUseCase.execute(CheckMsisdnPhoneNumberUseCase.getParams(phoneNumber),
                    new CheckMsisdnRegisterPhoneNumberSubscriber(getView(), phoneNumber));
    }


    private boolean isValid(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            getView().showErrorPhoneNumber(R.string.error_field_required_phone);
            return false;
        }
        if (phoneNumber.length() < COUNT_MIN) {
            getView().showErrorPhoneNumber(getView().getContext().getString(R.string.error_char_count_under));
            return false;
        }

        if (phoneNumber.length() > COUNT_MAX) {
            getView().showErrorPhoneNumber(getView().getContext().getString(R.string.error_char_count_over));
            return false;
        }

        return true;
    }
}
