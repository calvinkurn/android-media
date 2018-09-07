package com.tokopedia.transaction.insurance.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.insurance.data.InsuranceTnCRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCUseCase extends UseCase<String> {

    private final InsuranceTnCRepository repository;

    @Inject
    public InsuranceTnCUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               InsuranceTnCRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {
        return repository.getInsuranceTnc();
    }

}
