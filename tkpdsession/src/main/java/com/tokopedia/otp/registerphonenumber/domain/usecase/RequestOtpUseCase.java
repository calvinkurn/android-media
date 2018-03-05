package com.tokopedia.otp.registerphonenumber.domain.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.otp.registerphonenumber.data.source.RegisterPhoneNumberOtpSource;
import com.tokopedia.otp.registerphonenumber.view.viewmodel.RequestOtpViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 5/3/18.
 */

public class RequestOtpUseCase extends UseCase<RequestOtpViewModel> {

    protected static final String PARAM_MODE = "mode";
    protected static final String PARAM_OTP_TYPE = "otp_type";
    protected static final String PARAM_MSISDN = "msisdn";
    protected static final String PARAM_EMAIL = "user_email";
    public static final String PARAM_USER_ID = "user_id";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";
    public static final String MODE_EMAIL = "email";

    public static final int OTP_TYPE_REGISTER_PHONE_NUMBER = 116;

    private RegisterPhoneNumberOtpSource otpSource;

    @Inject
    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             RegisterPhoneNumberOtpSource otpSource) {
        super(threadExecutor, postExecutionThread);
        this.otpSource = otpSource;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return otpSource.requestRegisterOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String mode, String phone) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_MODE, mode);
        param.putInt(PARAM_OTP_TYPE, OTP_TYPE_REGISTER_PHONE_NUMBER);
        param.putString(PARAM_MSISDN, phone);
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), param
                .getParameters()));
        return param;
    }

}
