package com.tokopedia.session.register.registerphonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.session.register.registerphonenumber.view.listener.AddNameListener;
import com.tokopedia.session.register.registerphonenumber.view.subscriber.AddNameSubscriber;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.LoginRegisterPhoneNumberUseCase;
import com.tokopedia.session.register.registerphonenumber.domain.usecase.RegisterPhoneNumberUseCase;

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
        getView().showLoading();
        loginRegisterPhoneNumberUseCase.execute(
                RegisterPhoneNumberUseCase.getParamsWithName(
                        getView().getPhoneNumber(), name),
                new AddNameSubscriber(getView()));
    }

    @Override
    public void detachView() {
        super.detachView();
        loginRegisterPhoneNumberUseCase.unsubscribe();
    }
}
