package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.data.SubmitImageModel;
import com.tokopedia.session.changephonenumber.domain.UploadImageRepository;

import rx.Observable;

/**
 * Created by nisie on 3/13/17.
 */

public class SubmitImageUseCase extends UseCase<SubmitImageModel> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_FILE_UPLOADED = "file_uploaded";

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
