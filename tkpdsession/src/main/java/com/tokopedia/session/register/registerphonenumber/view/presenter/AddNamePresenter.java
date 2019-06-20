package com.tokopedia.session.register.registerphonenumber.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.changename.domain.usecase.ChangeNameUseCase;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;
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
    private ChangeNameUseCase changeNameUseCase;

    @Inject
    public AddNamePresenter(LoginRegisterPhoneNumberUseCase loginRegisterPhoneNumberUseCase,
                            ChangeNameUseCase changeNameUseCase,
                            UserSession userSession) {
        this.loginRegisterPhoneNumberUseCase = loginRegisterPhoneNumberUseCase;
        this.changeNameUseCase = changeNameUseCase;
        this.userSession = userSession;
    }

    @Override
    public void attachView(AddNameListener.View view) {
        super.attachView(view);
    }

    @Override
    public void initView() {

    }

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
            changeNameUseCase.execute(
                    ChangeNameUseCase.getParams(
                            userSession.getUserId().isEmpty() ? userSession.getTemporaryUserId() :
                                    userSession.getUserId(),
                            name
                    ),
                    new Subscriber<ChangeNameViewModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().dismissLoading();
                            getView().onErrorRegister(ErrorHandler.getErrorMessage(e));
                        }

                        @Override
                        public void onNext(ChangeNameViewModel changeNameViewModel) {
                            if (changeNameViewModel.isSuccess()) {
                                getView().dismissLoading();
                                getView().onSuccessAddName();
                            } else {
                                getView().dismissLoading();
                                getView().onErrorRegister(getView().getContext().getString(R
                                        .string.default_request_error_unknown));
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        loginRegisterPhoneNumberUseCase.unsubscribe();
        changeNameUseCase.unsubscribe();
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
