package com.tokopedia.session.register.domain.interactor.registerphonenumber;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.register.data.model.RegisterPhoneNumberModel;
import com.tokopedia.session.register.data.repository.RegisterPhoneNumberRepository;

import rx.Observable;

/**
 * @author by yfsx on 28/02/18.
 */

public class RegisterPhoneNumberUseCase extends UseCase<RegisterPhoneNumberModel> {

    private static final String PARAMS_PHONE_NUMBER = "phone";
    private static final String PARAMS_TPYE = "type";
    private static final String PARAMS_TPYE_PHONE = "phone";
    private static final String PARAMS_OS_TYPE = "os_type";
    private static final int PARAMS_OS_TYPE_ANDROID = 1;

    private final RegisterPhoneNumberRepository registerPhoneNumberRepository;

    public RegisterPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      RegisterPhoneNumberRepository registerPhoneNumberRepository) {
        super(threadExecutor, postExecutionThread);
        this.registerPhoneNumberRepository = registerPhoneNumberRepository;
    }

    @Override
    public Observable<RegisterPhoneNumberModel> createObservable(RequestParams requestParams) {
        return registerPhoneNumberRepository.registerPhoneNumber(requestParams.getParameters());
    }

    public static RequestParams getRegisterPhoneNumberRequestParams(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_PHONE_NUMBER, phoneNumber);
        params.putString(PARAMS_TPYE, PARAMS_TPYE_PHONE);
        params.putInt(PARAMS_OS_TYPE, PARAMS_OS_TYPE_ANDROID);
        return params;
    }
}
