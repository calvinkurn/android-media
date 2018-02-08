package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.GenerateHostDomain;

import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostDomain> {
    public static final int STATIC_GOLANG_VALUE = 2;
    public static final String PARAM_SERVER_LANGUAGE = "new_add";

    private GenerateHostUploadRepository generateHostUploadRepository;

    public GenerateHostUseCase(ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread,
                               GenerateHostUploadRepository generateHostUploadRepository) {
        super(threadExecutor, postExecutionThread);
        this.generateHostUploadRepository = generateHostUploadRepository;
    }

    @Override
    public Observable<GenerateHostDomain> createObservable(RequestParams requestParams) {
        return generateHostUploadRepository.generateHost(requestParams.getParameters());
    }
}
