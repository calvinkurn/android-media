package com.tokopedia.session.changephonenumber.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.UploadHostModel;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepository;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class GetUploadHostUseCase extends UseCase<UploadHostModel> {

    public static final String PARAM_NEW_ADD = "new_add";
    public static final String DEFAULT_NEW_ADD = "1";


    private final UploadImageRepository uploadImageRepository;

    public GetUploadHostUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<UploadHostModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.getUploadHost(requestParams.getParameters());
    }
}