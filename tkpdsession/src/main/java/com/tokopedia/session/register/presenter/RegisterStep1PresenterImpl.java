package com.tokopedia.session.register.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.NetworkUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.interactor.RegisterNetworkInteractor;
import com.tokopedia.session.register.util.RegisterUtil;
import com.tokopedia.session.register.viewlistener.RegisterStep1ViewListener;

import java.net.UnknownHostException;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/27/17.
 */
public class RegisterStep1PresenterImpl implements RegisterStep1Presenter, RegisterConstant {

    private final RegisterStep1ViewListener viewListener;
    private CompositeSubscription compositeSubscription;
    private RegisterNetworkInteractor networkInteractor;

    public RegisterStep1PresenterImpl(RegisterStep1ViewListener viewListener,
                                      CompositeSubscription compositeSubscription,
                                      RegisterNetworkInteractor networkInteractor) {
        this.viewListener = viewListener;
        this.compositeSubscription = compositeSubscription;
        this.networkInteractor = networkInteractor;
    }

    @Override
    public void registerNext() {
        viewListener.resetError();

        if (isValidForm() && hasInternetConnection()) {
            viewListener.setActionsEnabled(false);
            validateEmail(getValidateEmailParam());
        }


    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private void validateEmail(TKPDMapParam<String, String> param) {
        viewListener.showLoadingProgress();
        compositeSubscription.add(networkInteractor.smartRegister(param)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            viewListener.onErrorValidateEmail(
                                    viewListener.getString(R.string.msg_no_connection));
                        } else if (e instanceof RuntimeException &&
                                e.getLocalizedMessage() != null &&
                                e.getLocalizedMessage().length() <= 3) {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorValidateEmail(
                                            viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorValidateEmail(
                                            viewListener.getString(R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorValidateEmail(
                                            viewListener.getString(R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorValidateEmail(
                                            viewListener.getString(R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorValidateEmail(
                                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                                }
                            }, Integer.parseInt(e.toString()));
                        } else if (e instanceof RuntimeException && e.getLocalizedMessage() != null) {
                            viewListener.onErrorValidateEmail(e.getLocalizedMessage());
                        } else {
                            viewListener.onErrorValidateEmail(
                                    viewListener.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Integer action) {
                        startAction(action);
                    }
                }));
    }

    private void startAction(Integer action) {
        switch (action) {
            case GO_TO_LOGIN:
                viewListener.goToLogin();
                break;
            case GO_TO_REGISTER:
                viewListener.goToRegisterStep2();
                break;
            case GO_TO_ACTIVATION_PAGE:
                viewListener.goToActivationPage();
                break;
            case GO_TO_RESET_PASSWORD:
                viewListener.goToResetPasswordPage();
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
            sendGTMRegisterError(viewListener.getActivity(), AppEventTracking.EventLabel.PASSWORD);
        } else if (password.length() < PASSWORD_MINIMUM_LENGTH) {
            viewListener.setPasswordError(viewListener.getString(R.string.error_invalid_password));
            isValid = false;
            sendGTMRegisterError(viewListener.getActivity(), AppEventTracking.EventLabel.PASSWORD);
        }

        if (TextUtils.isEmpty(email)) {
            viewListener.setEmailError(viewListener.getString(R.string.error_field_required));
            isValid = false;
            sendGTMRegisterError(viewListener.getActivity(), AppEventTracking.EventLabel.EMAIL);
        } else if (!CommonUtils.EmailValidation(email)) {
            viewListener.setEmailError(viewListener.getString(R.string.error_invalid_email));
            isValid = false;
            sendGTMRegisterError(viewListener.getActivity(), AppEventTracking.EventLabel.EMAIL);
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

        return isValid;
    }

    private void sendGTMRegisterError(Context context, String label) {
        UnifyTracking.eventRegisterError(label);

    }
}
