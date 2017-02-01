package com.tokopedia.session.register.presenter;

import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.interactor.RegisterNetworkInteractor;
import com.tokopedia.session.register.model.RegisterViewModel;
import com.tokopedia.session.register.model.gson.RegisterResult;
import com.tokopedia.session.register.viewlistener.RegisterStep2ViewListener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterStep2PresenterImpl implements RegisterStep2Presenter, RegisterConstant {

    private final RegisterStep2ViewListener viewListener;
    private RegisterViewModel registerViewModel;
    private RegisterNetworkInteractor networkInteractor;
    private CompositeSubscription compositeSubscription;

    public RegisterStep2PresenterImpl(RegisterStep2ViewListener viewListener,
                                      RegisterNetworkInteractor networkInteractor,
                                      CompositeSubscription compositeSubscription) {
        this.viewListener = viewListener;
        this.registerViewModel = new RegisterViewModel();
        this.networkInteractor = networkInteractor;
        this.compositeSubscription = compositeSubscription;
    }

    @Override
    public void finishRegister() {
        viewListener.showLoadingProgress();
        viewListener.resetError();
        if (isValidForm()) {
            viewListener.dropKeyboard();
            sendGTMClickStepTwo();
            doFinishRegister();
        }

    }

    private void doFinishRegister() {
        compositeSubscription.add(
                networkInteractor.finishRegister(AuthUtil.generateParamsNetwork(viewListener.getActivity(),getRegisterStep2Param()))
                        .subscribeOn(Schedulers.newThread())
                        .unsubscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Response<TkpdResponse>>() {
                            @Override

                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e instanceof UnknownHostException) {
                                    viewListener.onErrorRegister(viewListener.getString(R.string.msg_no_connection));
                                } else if (e instanceof SocketTimeoutException) {
                                    viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_timeout));
                                } else if (e instanceof IOException) {
                                    viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_internal_server));
                                } else {
                                    viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_unknown));
                                }
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> responseData) {
                                if (responseData.isSuccessful()) {
                                    if (!responseData.body().isError()) {
                                        viewListener.onSuccessRegister(responseData.body().convertDataObj(RegisterResult.class));
                                    } else {
                                        viewListener.onErrorRegister(responseData.body().getErrorMessageJoined());
                                    }
                                } else {
                                    new ErrorHandler(new ErrorListener() {
                                        @Override
                                        public void onUnknown() {
                                            viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_unknown));
                                        }

                                        @Override
                                        public void onTimeout() {
                                            viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_timeout));
                                        }

                                        @Override
                                        public void onServerError() {
                                            viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_internal_server));
                                        }

                                        @Override
                                        public void onBadRequest() {
                                            viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_bad_request));
                                        }

                                        @Override
                                        public void onForbidden() {
                                            viewListener.onErrorRegister(viewListener.getString(R.string.default_request_error_forbidden_auth));
                                        }
                                    }, responseData.code());
                                }
                            }
                        }));
    }

    private TKPDMapParam<String, String> getRegisterStep2Param() {
        viewListener.setRegisterModel(registerViewModel);

        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_BIRTHDAY, String.valueOf(registerViewModel.getDateDay()));
        param.put(PARAM_BIRTHMONTH, String.valueOf(registerViewModel.getDateMonth()));
        param.put(PARAM_BIRTHYEAR, String.valueOf(registerViewModel.getDateYear()));
        param.put(PARAM_CONFIRM_PASSWORD, registerViewModel.getConfirmPassword());
        param.put(PARAM_EMAIL, registerViewModel.getRegisterStep1ViewModel().getEmail());
        param.put(PARAM_FACEBOOK_USERID, "");
        param.put(PARAM_FULLNAME, registerViewModel.getRegisterStep1ViewModel().getName());
        param.put(PARAM_GENDER, String.valueOf(registerViewModel.getGender()));
        param.put(PARAM_PASSWORD, registerViewModel.getRegisterStep1ViewModel().getPassword());
        param.put(PARAM_PHONE, registerViewModel.getPhone());
        param.put(PARAM_IS_AUTO_VERIFY, String.valueOf((registerViewModel.getRegisterStep1ViewModel().isAutoVerify() ? 1 : 0)));// change to this "1" to auto verify
        return param;
    }

    private void sendGTMClickStepTwo() {
        UnifyTracking.eventRegister(AppEventTracking.EventLabel.REGISTER_STEP_2);
    }

    @Override
    public RegisterViewModel getViewModel() {
        return registerViewModel;
    }

    @Override
    public void calculateDateTime() {
        Calendar now = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        maxDate.set(maxDate.get(Calendar.YEAR) - 14, maxDate.getMaximum(Calendar.MONTH), maxDate.getMaximum(Calendar.DATE));
        minDate.set(1933, minDate.getMinimum(Calendar.MONTH), minDate.getMinimum(Calendar.DATE));
        long maxtime = maxDate.getTimeInMillis();
        long mintime = minDate.getTimeInMillis();
        int dateYear = maxDate.get(Calendar.YEAR);
        int dateMonth = minDate.get(Calendar.MONTH) + 1;
        int dateDay = minDate.get(Calendar.DATE);

        registerViewModel.setDateYear(dateYear);
        registerViewModel.setDateMonth(dateMonth);
        registerViewModel.setDateDay(dateDay);
        registerViewModel.setMinDate(mintime);
        registerViewModel.setMaxDate(maxtime);
    }

    private boolean isValidForm() {
        boolean isValid = true;

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

        if (viewListener.getGender().getText().length() == 0) {
            viewListener.setGenderError(viewListener.getString(com.tokopedia.core.R.string.message_need_to_select_gender));
            sendGTMRegisterError(AppEventTracking.EventLabel.GENDER);
            isValid = false;
        }

        return isValid;
    }

    private void sendGTMRegisterError(String label) {
        UnifyTracking.eventRegisterError(label);
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        for (int i = MIN_PHONE_NUMBER; i <= MAX_PHONE_NUMBER; i++) {
            if (phoneNo.matches("\\d{" + i + "}")) return true;
        }
        return false;

    }

}
