package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.session.data.repository.SessionRepository;

import rx.Observable;

/**
 * @author by nisie on 10/21/17.
 */

public class RequestOtpUseCase extends UseCase<RequestOtpViewModel> {

    private static final String PARAM_MODE = "mode";
    private static final String PARAM_OTP_TYPE = "otp_type";
    private static final String PARAM_MSISDN = "msisdn";
    private static final String PARAM_EMAIL = "user_email";
    public static final String PARAM_USER_ID = "user_id";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";

    public static final int OTP_TYPE_SECURITY_QUESTION = 13;
    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 11;

    private final SessionRepository sessionRepository;

    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             SessionRepository sessionRepository) {
        super(threadExecutor, postExecutionThread);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return sessionRepository.requestOtp(requestParams.getParameters());
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

    public static RequestParams getParamBeforeLogin(String mode, int otpType, String tempUserId) {
        RequestParams param = RequestParams.create();
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters()));
        param.putString(PARAM_MODE, mode);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_USER_ID, tempUserId);
        return param;
    }

    public static RequestParams getParamEmailBeforeLogin(String mode, String email, int otpType,
                                                         String tempUserId) {
        RequestParams param = RequestParams.create();
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters()));
        param.putString(PARAM_MODE, mode);
        param.putString(PARAM_EMAIL, email);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_USER_ID, tempUserId);
        return param;
    }
}
