package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 04/01/18.
 */

public class ValidateEmailCodeUseCase extends UseCase<Boolean> {
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String OS_TYPE_ANDROID = "1";
    private static final String PARAM_OTP_CODE = "verif_code";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public ValidateEmailCodeUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ChangePhoneNumberRepository changePhoneNumberRepository) {
        super(threadExecutor, postExecutionThread);
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.validateEmailCode(requestParams.getParameters());
    }

    public static RequestParams getValidateEmailCodeParam(String otpCode) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_OTP_CODE, otpCode);
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }
}
