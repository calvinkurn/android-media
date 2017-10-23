package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.otp.data.RequestOtpModel;
import com.tokopedia.otp.domain.OtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 * @deprecated use RequestOtpUseCase instead
 */

@Deprecated
public class OldRequestOtpUseCase extends UseCase<RequestOtpModel> {

    public static final String PARAM_MODE = "mode";
    public static final String PARAM_OTP_TYPE = "otp_type";
    public static final String PARAM_MSISDN = "msisdn";
    private static final String PARAM_EMAIL = "user_email";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";

    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 13;

    private final OtpRepository otpRepository;

    public OldRequestOtpUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                OtpRepository otpRepository) {
        super(threadExecutor, postExecutionThread);
        this.otpRepository = otpRepository;
    }

    @Override
    public Observable<RequestOtpModel> createObservable(RequestParams requestParams) {
        return otpRepository.oldRequestOtp(requestParams.getParameters());
    }

    public static RequestParams getParamSMS(String mode, String phone, int otp_type) {
        RequestParams param = RequestParams.create();
        param.putString(OldRequestOtpUseCase.PARAM_MODE, mode);
        param.putInt(OldRequestOtpUseCase.PARAM_OTP_TYPE, otp_type);
        param.putString(OldRequestOtpUseCase.PARAM_MSISDN, phone);
        return param;
    }

    public static RequestParams getParamBeforeLogin(String mode, int otp_type) {
        RequestParams param = RequestParams.create();
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters()));
        param.putString(OldRequestOtpUseCase.PARAM_MODE, mode);
        param.putInt(OldRequestOtpUseCase.PARAM_OTP_TYPE, otp_type);
        return param;
    }

    public static RequestParams getParamEmailBeforeLogin(String mode, String email, int otp_type) {
        RequestParams param = RequestParams.create();
        param.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters()));
        param.putString(OldRequestOtpUseCase.PARAM_MODE, mode);
        param.putString(OldRequestOtpUseCase.PARAM_EMAIL, email);
        param.putInt(OldRequestOtpUseCase.PARAM_OTP_TYPE, otp_type);
        return param;
    }
}
