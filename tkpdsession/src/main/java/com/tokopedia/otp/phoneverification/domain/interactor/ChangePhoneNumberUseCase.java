package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.phoneverification.data.ChangePhoneNumberModel;
import com.tokopedia.otp.phoneverification.domain.MsisdnRepository;

import rx.Observable;

/**
 * Created by nisie on 5/10/17.
 */

public class ChangePhoneNumberUseCase extends UseCase<ChangePhoneNumberModel> {

    public static final String PARAM_MSISDN = "msisdn";
    private final MsisdnRepository msisdnRepository;

    public ChangePhoneNumberUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    MsisdnRepository msisdnRepository) {
        super(threadExecutor, postExecutionThread);
        this.msisdnRepository = msisdnRepository;
    }

    @Override
    public Observable<ChangePhoneNumberModel> createObservable(RequestParams requestParams) {
        return msisdnRepository.changeMsisdn(requestParams.getParameters());
    }
}
