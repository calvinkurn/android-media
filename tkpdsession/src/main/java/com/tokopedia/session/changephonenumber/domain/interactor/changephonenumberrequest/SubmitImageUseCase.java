package com.tokopedia.session.changephonenumber.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.domain.model.changephonenumberrequest.SubmitImageModel;
import com.tokopedia.session.changephonenumber.data.repository.UploadImageRepository;

import rx.Observable;

/**
 * Created by nisie on 3/13/17.
 */

public class SubmitImageUseCase extends UseCase<SubmitImageModel> {

    public static final String PARAM_FILE_UPLOADED = "file_uploaded";
    public static final String PARAM_OS_TYPE = "os_type";
    public static final String DEFAULT_OS_TYPE = "1";


    private final UploadImageRepository uploadImageRepository;

    public SubmitImageUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<SubmitImageModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.submitImage(requestParams.getParameters());
    }
}
