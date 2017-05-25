package com.tokopedia.session.register.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.register.data.RegisterEmailModel;
import com.tokopedia.session.register.domain.RegisterEmailRepository;

import rx.Observable;

/**
 * Created by nisie on 4/13/17.
 */

public class RegisterEmailUseCase extends UseCase<RegisterEmailModel> {

    public static final String PARAM_CONFIRM_PASSWORD = "confirm_password";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_FULLNAME = "full_name";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_PHONE = "phone";
    public static final String PARAM_IS_AUTO_VERIFY = "is_auto_verify";

    private final RegisterEmailRepository registerEmailRepository;

    public RegisterEmailUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                RegisterEmailRepository registerEmailRepository) {
        super(threadExecutor, postExecutionThread);
        this.registerEmailRepository = registerEmailRepository;
    }

    @Override
    public Observable<RegisterEmailModel> createObservable(RequestParams requestParams) {
        return registerEmailRepository.registerEmail(requestParams.getParameters());
    }
}