package com.tokopedia.session.register.registerphonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.R;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.LoginRegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.RegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.view.listener.AddNameListener;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.AddNameSubscriber;
import com.tokopedia.session.register.view.util.RegisterUtil;

import javax.inject.Inject;

/**
 * @author by yfsx on 22/03/18.
 */

public class AddNamePresenter
        extends BaseDaggerPresenter<AddNameListener.View>
        implements AddNameListener.Presenter {

    private LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase;

    @Inject
    public AddNamePresenter(LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase) {
        this.loginRegisterPhoneNumberUseCase = loginRegisterPhoneNumberUseCase;
    }

    @Override
    public void attachView(AddNameListener.View view) {
        super.attachView(view);
    }

    @Override
    public void initView() {

    }

    @Override
    public void registerPhoneNumberAndName(String name) {
        if (isValidate(name)) {
            getView().showLoading();
            loginRegisterPhoneNumberUseCase.execute(
                    RegisterPhoneNumberUseCase.getParamsWithName(
                            getView().getPhoneNumber(), name),
                    new AddNameSubscriber(getView()));
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
