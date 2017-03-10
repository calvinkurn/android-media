package com.tokopedia.session.changephonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changephonenumber.data.GeneratePostKeyModel;
import com.tokopedia.session.changephonenumber.data.UploadHostModel;
import com.tokopedia.session.changephonenumber.domain.UploadImageRepository;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class GeneratePostKeyUseCase extends UseCase<GeneratePostKeyModel> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_KTP_IMAGE_ID = "ktp_image_id";
    public static final String PARAM_BANKBOOK_IMAGE_ID = "bankbook_image_id";
    private final UploadImageRepository uploadImageRepository;

    public GeneratePostKeyUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<GeneratePostKeyModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.generatePostKey(requestParams.getParameters());
    }
}
