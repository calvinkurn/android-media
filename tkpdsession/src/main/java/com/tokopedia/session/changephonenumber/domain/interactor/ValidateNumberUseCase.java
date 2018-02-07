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
    private static final String PARAM_OS_TYPE = "os_type";
    private static final String OS_TYPE_ANDROID = "1";
    private static final String PARAM_ACTION = "action";
    private static final String PARAM_NEW_MSISDN = "new_msisdn";
    private static final String ACTION_VALIDATE = "validate";
    private static final String ACTION_SUMBIT = "submit";

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

    public static RequestParams getValidateNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_ACTION, ACTION_VALIDATE);
        param.putString(PARAM_NEW_MSISDN, newPhoneNumber);
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }

    public static RequestParams getSubmitNumberParam(String newPhoneNumber) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_ACTION, ACTION_SUMBIT);
        param.putString(PARAM_NEW_MSISDN, newPhoneNumber);
        param.putString(PARAM_OS_TYPE, OS_TYPE_ANDROID);
        return param;
    }
}
