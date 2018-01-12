package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.domain.ChangePhoneNumberRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by milhamj on 03/01/18.
 */

public class ValidateNumberUseCase extends UseCase<Boolean> {
    public static final String PARAM_ACTION = "action";
    public static final String PARAM_NEW_MSISDN = "new_msisdn";
    public static final String ACTION_VALIDATE = "validate";
    public static final String ACTION_SUMBIT = "submit";

    private final ChangePhoneNumberRepository changePhoneNumberRepository;

    @Inject
    public ValidateNumberUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            ChangePhoneNumberRepository changePhoneNumberRepository) {
        super(threadExecutor, postExecutionThread);
        this.changePhoneNumberRepository = changePhoneNumberRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return changePhoneNumberRepository.validateNumber(requestParams.getParameters());
    }
}
