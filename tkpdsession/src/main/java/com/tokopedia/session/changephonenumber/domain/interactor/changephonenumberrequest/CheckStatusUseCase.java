package com.tokopedia.session.changephonenumber.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.CheckStatusModel;
import com.tokopedia.session.changephonenumber.data.repository.KtpRepository;

import rx.Observable;

/**
 * Created by nisie on 3/10/17.
 */

public class CheckStatusUseCase extends UseCase<CheckStatusModel> {

    private final KtpRepository ktpRepository;

    public CheckStatusUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              KtpRepository ktpRepository) {
        super(threadExecutor, postExecutionThread);
        this.ktpRepository = ktpRepository;
    }

    @Override
    public Observable<CheckStatusModel> createObservable(RequestParams requestParams) {
        return ktpRepository.checkStatus(requestParams.getParameters());
    }
}