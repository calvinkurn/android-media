package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.domain.interactor.registerthird.CreatePasswordUseCase;
import com.tokopedia.session.register.view.subscriber.CreatePasswordSubscriber;
import com.tokopedia.session.register.view.viewlistener.CreatePassword;
import com.tokopedia.session.register.view.viewmodel.createpassword.CreatePasswordModel;
import com.tokopedia.session.session.presenter.RegisterNewImpl;
import com.tokopedia.session.session.presenter.RegisterNewNextImpl;

import javax.inject.Inject;

import static android.R.attr.mode;

/**
 * @author by nisie on 10/13/17.
 */

public class CreatePasswordPresenter extends BaseDaggerPresenter<CreatePassword.View>
        implements CreatePassword.Presenter {

    private final CreatePasswordUseCase createPasswordUseCase;
    private final SessionHandler sessionHandler;
    private CreatePassword.View viewListener;


    @Inject
    public CreatePasswordPresenter(CreatePasswordUseCase createPasswordUseCase,
                                   SessionHandler sessionHandler) {
        this.createPasswordUseCase = createPasswordUseCase;
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
        createPasswordUseCase.unsubscribe();
    }

    @Override
    public void createPassword(CreatePasswordModel model) {
        viewListener.resetError();
        if (isValid(model)) {
            createPasswordUseCase.execute(
                    CreatePasswordUseCase.getParam(
                            model.getFullName(),
                            model.getBdayDay(),
                            model.getBdayMonth(),
                            model.getBdayYear(),
                            model.getNewPass(),
                            model.getConfirmPass(),
                            model.getMsisdn(),
                            model.getRegisterTos(),
                            sessionHandler.getTempLoginSession(MainApplication.getAppContext())
                    ),
                    new CreatePasswordSubscriber(viewListener));
        }
    }

    private boolean isValid(CreatePasswordModel model) {
        boolean isValid = true;

        if (TextUtils.isEmpty(model.getRegisterTos())
                || model.getRegisterTos().equals("0")) {
            viewListener.showErrorTOS();
            isValid = false;
        }

        if (TextUtils.isEmpty(model.getMsisdn())) {
            viewListener.showErrorPhoneNumber(R.string.error_field_required);
            isValid = false;
        } else if (!RegisterNewNextImpl.isValidPhoneNumber(model.getMsisdn())) {
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
        } else if (RegisterNewImpl.RegisterUtil.checkRegexNameLocal(model.getFullName())) {
            viewListener.showErrorName(R.string.error_illegal_character);
            isValid = false;
        } else if (model.getFullName().length() > 35) {
            viewListener.showErrorName(R.string.error_max_35_character);
            isValid = false;
        }

        return isValid;
    }
}
