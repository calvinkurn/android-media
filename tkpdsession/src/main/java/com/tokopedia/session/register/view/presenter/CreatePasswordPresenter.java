package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.domain.interactor.registerthird.CreatePasswordLoginUseCase;
import com.tokopedia.session.register.domain.interactor.registerthird.CreatePasswordUseCase;
import com.tokopedia.session.register.view.subscriber.CreatePasswordSubscriber;
import com.tokopedia.session.register.view.util.RegisterUtil;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 10/13/17.
 */

public class CreatePasswordPresenter extends BaseDaggerPresenter<CreatePassword.View>
        implements CreatePassword.Presenter {

    private final SessionHandler sessionHandler;
    private final CreatePasswordLoginUseCase createPasswordLoginUseCase;
    private CreatePassword.View viewListener;
    private int MAX_LENGTH_NAME = 35;


    @Inject
    public CreatePasswordPresenter(CreatePasswordLoginUseCase createPasswordLoginUseCase,
                                   SessionHandler sessionHandler) {
        this.createPasswordLoginUseCase = createPasswordLoginUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(CreatePassword.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        createPasswordLoginUseCase.unsubscribe();
    }

    @Override
    public void createPassword(CreatePasswordViewModel model) {
        viewListener.resetError();
        if (isValid(model)) {
            createPasswordLoginUseCase.execute(
                    CreatePasswordUseCase.getParam(
                            model.getFullName(),
                            model.getBdayDay(),
                            model.getBdayMonth(),
                            model.getBdayYear(),
                            model.getNewPass(),
                            model.getConfirmPass(),
                            model.getMsisdn(),
                            model.getRegisterTos(),
                            model.getLoginId()
                    ),
                    new CreatePasswordSubscriber(viewListener));
        }
    }

    private boolean isValid(CreatePasswordViewModel model) {
        boolean isValid = true;

        if (TextUtils.isEmpty(model.getRegisterTos())
                || model.getRegisterTos().equals("0")) {
            viewListener.showErrorTOS();
            isValid = false;
        }

        if (TextUtils.isEmpty(model.getMsisdn())) {
            viewListener.showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        } else if (!RegisterUtil.isValidPhoneNumber(model.getMsisdn())) {
            viewListener.showErrorPhoneNumber(R.string.error_invalid_phone_number);
            isValid = false;
        }

        if (TextUtils.isEmpty(model.getConfirmPass())) {
            viewListener.showErrorConfirmPassword(R.string.error_field_required);
            isValid = false;
        } else if (!model.getConfirmPass().equals(model.getNewPass())) {
            viewListener.showErrorConfirmPassword(R.string.error_password_not_same);
            isValid = false;
        }

        if (TextUtils.isEmpty(model.getNewPass())) {
            viewListener.showErrorPassword(R.string.error_field_required);
            isValid = false;
        }

        if (model.getBdayYear() == 0
                || model.getBdayMonth() == 0
                || model.getBdayDay() == 0) {
            viewListener.showErrorBday(R.string.error_field_required);
            isValid = false;
        }

        if (TextUtils.isEmpty(model.getFullName())) {
            viewListener.showErrorName(R.string.error_field_required);
            isValid = false;
        } else if (RegisterUtil.checkRegexNameLocal(model.getFullName())) {
            viewListener.showErrorName(R.string.error_illegal_character);
            isValid = false;
        } else if (model.getFullName().length() > MAX_LENGTH_NAME) {
            viewListener.showErrorName(R.string.error_max_35_character);
            isValid = false;
        }

        return isValid;
    }
}
