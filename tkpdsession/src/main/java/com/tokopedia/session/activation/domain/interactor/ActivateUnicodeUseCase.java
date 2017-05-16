package com.tokopedia.session.activation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.activation.data.ActivateUnicodeModel;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodeUseCase extends UseCase<ActivateUnicodeModel> {

    public static final String PARAM_UNICODE = "password";
    public static final String PARAM_EMAIL = "username";
    public static final String PARAM_GRANT_TYPE = "grant_type";
    public static final String PARAM_PASSWORD_TYPE = "password_type";
    public static final String DEFAULT_GRANT_TYPE = "password";
    public static final String DEFAULT_PASSWORD_TYPE = "activation_unique_code";


    private final RegisterActivationRepository registerActivationRepository;

    public ActivateUnicodeUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  RegisterActivationRepository registerActivationRepository) {
        super(threadExecutor, postExecutionThread);
        this.registerActivationRepository = registerActivationRepository;
    }

    @Override
    public Observable<ActivateUnicodeModel> createObservable(RequestParams requestParams) {
        return registerActivationRepository.activateWithUnicode(requestParams.getParameters());
    }
}