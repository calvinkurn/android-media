package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostModel> {

    public static final int STATIC_GOLANG_VALUE = 2;
    public static final String PARAM_SERVER_LANGUAGE = "new_add";

    private final UploadImageRepository uploadImageRepository;

    public GenerateHostUseCase(ThreadExecutor jobExecutor,
                               PostExecutionThread uiThread,
                               UploadImageRepository uploadImageRepository) {
        super(jobExecutor, uiThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<GenerateHostModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.generateHost(
                requestParams.getParameters());
    }
}