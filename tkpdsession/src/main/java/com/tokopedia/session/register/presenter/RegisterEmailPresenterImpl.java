package com.tokopedia.session.register.presenter;

import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.NetworkUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.data.factory.RegisterEmailSourceFactory;
import com.tokopedia.session.register.data.mapper.RegisterEmailMapper;
import com.tokopedia.session.register.data.repository.RegisterEmailRepositoryImpl;
import com.tokopedia.session.register.domain.interactor.RegisterEmailUseCase;
import com.tokopedia.session.register.model.RegisterViewModel;
import com.tokopedia.session.register.subscriber.RegisterEmailSubscriber;
import com.tokopedia.session.register.util.RegisterUtil;
import com.tokopedia.session.register.viewlistener.RegisterEmailViewListener;

/**
 * Created by nisie on 1/27/17.
 */
public class RegisterEmailPresenterImpl implements RegisterEmailPresenter, RegisterConstant {

    private final RegisterEmailViewListener viewListener;
    private RegisterViewModel registerViewModel;
    private RegisterEmailUseCase registerEmailUseCase;

    public RegisterEmailPresenterImpl(RegisterEmailViewListener viewListener) {
        this.viewListener = viewListener;
        this.registerViewModel = new RegisterViewModel();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
        String authKey = sessionHandler.getAccessToken(viewListener.getActivity());
        authKey = sessionHandler.getTokenType(viewListener.getActivity()) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);
        RegisterEmailRepositoryImpl registerEmailRepository = new RegisterEmailRepositoryImpl(
                new RegisterEmailSourceFactory(
                        viewListener.getActivity(),
                        accountsService,
                        new RegisterEmailMapper()
                ));

        this.registerEmailUseCase = new RegisterEmailUseCase(
                new JobExecutor(), new UIThread(), registerEmailRepository);
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
        viewListener.setRegisterModel(registerViewModel);

        param.putString(PARAM_EMAIL, registerViewModel.getEmail());
        param.putString(PARAM_FULLNAME, registerViewModel.getName());
        param.putString(PARAM_PASSWORD, registerViewModel.getPassword());
        param.putString(PARAM_CONFIRM_PASSWORD, registerViewModel.getPassword());
        param.putString(PARAM_PHONE, registerViewModel.getPhone());
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
    public void startAction(int action) {
        switch (action) {
            case GO_TO_LOGIN:
                viewListener.goToAutomaticLogin();
                break;
            case GO_TO_REGISTER:
                viewListener.goToRegisterStep2();
                break;
            case GO_TO_ACTIVATION_PAGE:
                viewListener.goToActivationPage();
                break;
            case GO_TO_RESET_PASSWORD:
                viewListener.goToAutomaticResetPassword();
                break;
            default:
                throw new RuntimeException("ERROR UNKNOWN ACTION");
        }
    }

    private TKPDMapParam<String, String> getValidateEmailParam() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("email", viewListener.getEmail().getText().toString());
        param.put("password", viewListener.getPassword().getText().toString());
        return param;
    }

    private boolean hasInternetConnection() {
        if (!NetworkUtil.isConnected(viewListener.getActivity())) {
            viewListener.showSnackbar(
                    viewListener.getString(R.string.alert_check_your_internet_connection));
        }
        return NetworkUtil.isConnected(viewListener.getActivity());
    }

    private boolean isValidForm() {
        boolean isValid = true;

        String name = viewListener.getName().getText().toString();
        String email = viewListener.getEmail().getText().toString();
        String password = viewListener.getPassword().getText().toString();

        if (TextUtils.isEmpty(password)) {
            viewListener.setPasswordError(viewListener.getString(R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        } else if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            viewListener.setPasswordError(viewListener.getString(R.string.error_invalid_password));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.PASSWORD);
        }

        if (TextUtils.isEmpty(viewListener.getPhone().getText().toString())) {
            viewListener.setPhoneError(viewListener.getString(com.tokopedia.core.R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
        } else {
            boolean validatePhoneNumber = validatePhoneNumber(viewListener.getPhone().getText().toString().replace("-", ""));
            if (!validatePhoneNumber) {
                viewListener.setPhoneError(viewListener.getString(com.tokopedia.core.R.string.error_invalid_phone_number));
                isValid = false;
                sendGTMRegisterError(AppEventTracking.EventLabel.HANDPHONE);
            }
        }

        // Check for a valid name.
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
