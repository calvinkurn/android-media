package com.tokopedia.otp.domain.oldinteractor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.domainold.OtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 * @deprecated use RequestOtpUseCase in interactor package instead.
 */
@Deprecated
public class RequestOtpUseCase extends UseCase<RequestOtpModel> {

    public static final String PARAM_MODE = "mode";
    public static final String PARAM_OTP_TYPE = "otp_type";
    public static final String PARAM_MSISDN = "msisdn";

    public static final String MODE_SMS = "sms";
    public static final String MODE_CALL = "call";

    private final OtpRepository otpRepository;

    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             OtpRepository otpRepository) {
        super(threadExecutor, postExecutionThread);
        this.otpRepository = otpRepository;
    }

    @Override
    public Observable<RequestOtpModel> createObservable(RequestParams requestParams) {
        return otpRepository.requestOtp(requestParams.getParameters());
    }
}
