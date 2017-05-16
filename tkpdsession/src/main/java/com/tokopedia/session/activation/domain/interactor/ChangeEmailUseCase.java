package com.tokopedia.session.activation.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.activation.data.ChangeEmailModel;
import com.tokopedia.session.activation.domain.RegisterActivationRepository;

import rx.Observable;

/**
 * Created by nisie on 4/18/17.
 */

public class ChangeEmailUseCase extends UseCase<ChangeEmailModel> {

    public static final String PARAM_OLD_EMAIL = "old_email";
    public static final String PARAM_NEW_EMAIL = "new_email";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_SEND_EMAIL = "send_email";
    public static final String DEFAULT_SEND_EMAIL = "1";

    private final RegisterActivationRepository registerActivationRepository;

    public ChangeEmailUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              RegisterActivationRepository registerActivationRepository) {
        super(threadExecutor, postExecutionThread);
        this.registerActivationRepository = registerActivationRepository;
    }

    @Override
    public Observable<ChangeEmailModel> createObservable(RequestParams requestParams) {
        return registerActivationRepository.changeEmail(requestParams.getParameters());
    }
}