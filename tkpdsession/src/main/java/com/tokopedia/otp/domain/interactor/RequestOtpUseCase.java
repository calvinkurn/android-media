package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.source.OtpSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/21/17.
 *         params :
 *         - mode : use static parameter from RequestOtpUseCase
 *         - otp_type : use static parameter from RequestOtpUseCase
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

    public static final int OTP_TYPE_SECURITY_QUESTION = 13;
    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 11;
    public static final int OTP_TYPE_CHANGE_PHONE_NUMBER = 20;

    protected final OtpSource otpSource;

    @Inject
    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             OtpSource otpSource) {
        super(threadExecutor, postExecutionThread);
        this.otpSource = otpSource;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return otpSource.requestOtp(requestParams.getParameters());
    }

    public static RequestParams getParamAfterLogin(String mode, String phone, int otpType) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_MODE, mode);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_MSISDN, phone);
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), param
                .getParameters()));
        return param;
    }

    public static RequestParams getParamBeforeLogin(String mode, String phone, int otpType, String
            tempUserId) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_MODE, mode);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_MSISDN, phone);
        param.putAll(AuthUtil.generateParamsNetworkObject(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters(), tempUserId));
        return param;
    }


}
