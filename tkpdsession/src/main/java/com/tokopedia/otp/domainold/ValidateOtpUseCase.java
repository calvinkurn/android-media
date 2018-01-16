package com.tokopedia.otp.domainold;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.data.model.ValidateOtpModel;

import rx.Observable;

/**
 * Created by nisie on 3/7/17.
 */
@Deprecated
public class ValidateOtpUseCase extends UseCase<ValidateOtpModel> {

    public static final String PARAM_USER = "user";
    public static final String PARAM_CODE = "code";

    private final OtpRepository otpRepository;

    public ValidateOtpUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              OtpRepository otpRepository) {
        super(threadExecutor, postExecutionThread);
        this.otpRepository = otpRepository;
    }

    @Override
    public Observable<ValidateOtpModel> createObservable(RequestParams requestParams) {
        return otpRepository.validateOtp(requestParams.getParameters());
    }
}