package com.tokopedia.otp.securityquestion.domain.interactor.changephonenumberrequest;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.securityquestion.data.model.changephonenumberrequest.ValidateImageModel;
import com.tokopedia.otp.securityquestion.data.repository.UploadImageRepository;

import rx.Observable;

/**
 * Created by nisie on 3/9/17.
 */

public class ValidateImageUseCase extends UseCase<ValidateImageModel> {

    public static final String PARAM_ID_WIDTH = "ktp_width";
    public static final String PARAM_ID_HEIGHT = "ktp_height";
    public static final String PARAM_BANKBOOK_WIDTH = "bankbook_width";
    public static final String PARAM_BANKBOOK_HEIGHT = "bankbook_height";

    private final UploadImageRepository uploadImageRepository;

    public ValidateImageUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                UploadImageRepository uploadImageRepository) {
        super(threadExecutor, postExecutionThread);
        this.uploadImageRepository = uploadImageRepository;
    }

    @Override
    public Observable<ValidateImageModel> createObservable(RequestParams requestParams) {
        return uploadImageRepository.validateImage(requestParams.getParameters());
    }
}
