package com.tokopedia.session.register.view.presenter;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.data.model.RegisterViewModel;
import com.tokopedia.session.register.domain.interactor.RegisterEmailUseCase;
import com.tokopedia.session.register.view.subscriber.RegisterEmailSubscriber;
import com.tokopedia.session.register.view.util.RegisterUtil;
import com.tokopedia.session.register.view.viewlistener.RegisterEmailViewListener;
import com.tokopedia.session.register.view.viewmodel.RegisterEmailViewModel;

/**
 * Created by nisie on 1/27/17.
 */
public class RegisterEmailPresenterImpl implements RegisterEmailPresenter, RegisterConstant {

    private static final int STATUS_ACTIVE = 1;
    private static final int STATUS_PENDING = -1;
    private static final int STATUS_INACTIVE = 0;

    private final RegisterEmailViewListener viewListener;
    private RegisterViewModel registerViewModel;
    private RegisterEmailUseCase registerEmailUseCase;

    public RegisterEmailPresenterImpl(RegisterEmailViewListener viewListener,
                                      RegisterEmailUseCase registerEmailUseCase,
                                      RegisterViewModel registerViewModel) {
        this.viewListener = viewListener;
        this.registerEmailUseCase = registerEmailUseCase;
        this.registerViewModel = registerViewModel;
    }

    @Override
    public void onRegisterClicked() {
        viewListener.resetError();
        if (isValidForm()) {
            viewListener.showLoadingProgress();
            viewListener.dropKeyboard();
            sendGTMClickStepTwo();
            registerEmail();
        }
    }

    private RequestParams getRegisterStep2Param() {
        RequestParams param = RequestParams.create();
        viewListener.getRegisterModel(registerViewModel);
        param.putString(RegisterEmailUseCase.PARAM_EMAIL, registerViewModel.getEmail());
        param.putString(RegisterEmailUseCase.PARAM_FULLNAME, registerViewModel.getName());
        param.putString(RegisterEmailUseCase.PARAM_PASSWORD, registerViewModel.getPassword());
        param.putString(RegisterEmailUseCase.PARAM_CONFIRM_PASSWORD, registerViewModel.getPassword());
        param.putString(RegisterEmailUseCase.PARAM_PHONE, registerViewModel.getPhone());
        param.putInt(RegisterEmailUseCase.PARAM_IS_AUTO_VERIFY, registerViewModel.getIsAutoVerify());
        return param;
    }

    private void registerEmail() {
        registerEmailUseCase.execute(getRegisterStep2Param(), new RegisterEmailSubscriber(viewListener));
    }

    private void sendGTMClickStepTwo() {
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_2);
    }

    @Override
    public void unsubscribeObservable() {
        registerEmailUseCase.unsubscribe();
    }

    @Override
    public void startAction(RegisterEmailViewModel viewModel) {
        switch (viewModel.getAction()) {
            case GO_TO_LOGIN:
                viewListener.goToAutomaticLogin();
                break;
            case GO_TO_REGISTER:
            case GO_TO_ACTIVATION_PAGE:
                if (viewModel.getIsActive() == STATUS_ACTIVE)
                    viewListener.goToAutomaticLogin();
                else if (viewModel.getIsActive() == STATUS_INACTIVE || viewModel.getIsActive() == STATUS_PENDING )
                    viewListener.goToActivationPage(viewModel);
                break;
            case GO_TO_RESET_PASSWORD:
                viewListener.showInfo();
                break;
            default:
                throw new RuntimeException("ERROR UNKNOWN ACTION");
        }

        if(registerViewModel != null) {
            UnifyTracking.eventMoRegister(registerViewModel.getName(), registerViewModel.getPhone());
        }
    }

    private boolean isValidForm() {
        boolean isValid = true;

        String name = viewListener.getName().getText().toString();
        String email = viewListener.getEmail().getText().toString();
        String password = viewListener.getPassword().getText().toString();
        String phone = viewListener.getPhone().getText().toString();

        if (TextUtils.isEmpty(password)) {
            viewListener.setPasswordError(viewListener.getString(R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        } else if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            viewListener.setPasswordError(viewListener.getString(R.string.error_invalid_password));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        }

        if (TextUtils.isEmpty(phone)) {
            viewListener.setPhoneError(viewListener.getString(com.tokopedia.core.R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
        } else {
            boolean validatePhoneNumber = validatePhoneNumber(phone.replace("-", ""));
            if (!validatePhoneNumber) {
                viewListener.setPhoneError(viewListener.getString(com.tokopedia.core.R.string.error_invalid_phone_number));
                isValid = false;
                sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            }
        }

        if (TextUtils.isEmpty(name)) {
            viewListener.setNameError(viewListener.getString(R.string.error_field_required));
            isValid = false;
        } else if (RegisterUtil.checkRegexNameLocal(name)) {
            viewListener.setNameError(viewListener.getString(R.string.error_illegal_character));
            isValid = false;
        } else if (RegisterUtil.isExceedMaxCharacter(name)) {
            viewListener.setNameError(viewListener.getString(R.string.error_max_35_character));
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            viewListener.setEmailError(viewListener.getString(R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
        } else if (!CommonUtils.EmailValidation(email)) {
            viewListener.setEmailError(viewListener.getString(R.string.error_invalid_email));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.EMAIL);
        }


        return isValid;
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        return false;

    }

    private void sendGTMRegisterError(String label) {
        UnifyTracking.eventRegisterError(label);




    }
}
