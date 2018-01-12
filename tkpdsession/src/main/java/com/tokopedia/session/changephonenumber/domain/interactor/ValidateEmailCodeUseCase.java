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

    public static RequestParams getSendEmailParam(String otpCode) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_OTP_CODE, otpCode);
        return param;
    }
}
