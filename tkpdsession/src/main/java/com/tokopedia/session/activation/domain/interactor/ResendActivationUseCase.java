package com.tokopedia.session.activation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.activation.data.ResendActivationModel;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;

import rx.Observable;

/**
 * Created by nisie on 4/17/17.
 */

public class ResendActivationUseCase extends UseCase<ResendActivationModel> {

    public static final String PARAM_EMAIL = "email";

    private final RegisterActivationRepository registerActivationRepository;

    public ResendActivationUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   RegisterActivationRepository registerActivationRepository) {
        super(threadExecutor, postExecutionThread);
        this.registerActivationRepository = registerActivationRepository;
    }

    @Override
    public Observable<ResendActivationModel> createObservable(RequestParams requestParams) {
        return registerActivationRepository.resendActivation(requestParams.getParameters());
    }
}