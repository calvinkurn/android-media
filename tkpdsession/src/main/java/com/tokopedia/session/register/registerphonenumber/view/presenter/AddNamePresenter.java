package com.tokopedia.session.register.registerphonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.LoginRegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.RegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.view.listener.AddNameListener;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.AddNameSubscriber;
import com.tokopedia.session.register.view.util.RegisterUtil;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNamePresenter
        extends BaseDaggerPresenter<AddNameListener.View>
        implements AddNameListener.Presenter {

    private final UserSession userSession;
    private LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase;

    @Inject
    public AddNamePresenter(LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase,
                            UserSession userSession) {
        this.loginRegisterPhoneNumberUseCase = loginRegisterPhoneNumberUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(AddNameListener.View view) {
        super.attachView(view);
    }

    @Override
    public void initView() {

    }

    @Deprecated
    @Override
    public void registerPhoneNumberAndName(String name, String uuid, String phoneNumber) {
        if (isValidate(name)) {
            getView().showLoading();
            loginRegisterPhoneNumberUseCase.execute(
                    RegisterPhoneNumberUseCase.getParamsWithName(
                            phoneNumber, name, uuid),
                    new AddNameSubscriber(getView()));
        }
    }

    @Override
    public void addName(String name) {
        if (isValidate(name)) {
            getView().showLoading();

        }
    }

    @Override
    public void detachView() {
        super.detachView();
        loginRegisterPhoneNumberUseCase.unsubscribe();
    }

    private boolean isValidate(String name) {
        if (name.length() < RegisterUtil.MIN_NAME) {
            getView().showValidationError(getView().getContext().getResources().getString(R.string.error_name_too_short));
            return false;
        }
        if (name.length() > RegisterUtil.MAX_NAME_128) {
            getView().showValidationError(getView().getContext().getResources().getString(R.string.error_name_too_long));
            return false;
        }
        getView().hideValidationError();
        return true;
    }
}
