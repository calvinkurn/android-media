package com.tokopedia.inbox.rescenter.discussion.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.UploadImageRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import rx.Observable;

/**
 * Created by nisie on 4/3/17.
 */

public class GenerateHostUseCase extends UseCase<GenerateHostModel> {

    private final UploadImageRepository uploadImageRepository;

    public GenerateHostUseCase(JobExecutor jobExecutor,
                               UIThread uiThread,
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