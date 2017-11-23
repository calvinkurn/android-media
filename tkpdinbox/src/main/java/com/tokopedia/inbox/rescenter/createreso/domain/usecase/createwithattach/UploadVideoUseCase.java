package com.tokopedia.inbox.rescenter.createreso.domain.usecase.createwithattach;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.rescenter.createreso.data.repository.GenerateHostUploadRepository;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.UploadDomain;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by yoasfs on 12/09/17.
 */

public class UploadVideoUseCase extends UploadUseCase {

    public UploadVideoUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              GenerateHostUploadRepository generateHostUploadRepository) {
        super(threadExecutor, postExecutionThread, generateHostUploadRepository);
    }

    @Override
    public Observable<UploadDomain> createObservable(RequestParams requestParams) {
        return generateHostUploadRepository.uploadVideo(generateUrl(requestParams),
                generateRequestBody(requestParams),
                getVideoFile(requestParams)
        );
    }

    @Override
    protected String generateUrl(RequestParams requestParams) {
        return HTTP
                + requestParams.getString(PARAM_GENERATED_HOST, "")
                + TkpdBaseURL.Upload.PATH_UPLOAD_VIDEO;
    }

    private MultipartBody.Part getVideoFile(RequestParams requestParams) {
        File file = new File(requestParams.getString(PARAM_FILE_TO_UPLOAD, ""));
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/mp4"), file);
        return MultipartBody.Part.createFormData("fileToUpload", file.getName(), requestBody);
    }

}
